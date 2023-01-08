package com.github.odaridavid.talkself.ui.chat

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.data.local.messages.toDomain
import com.github.odaridavid.talkself.databinding.FragmentChatBinding
import com.github.odaridavid.talkself.domain.toUiModel
import com.github.odaridavid.talkself.ui.models.ConversationUiModel
import com.github.odaridavid.talkself.ui.models.MessageUiModel
import com.github.odaridavid.talkself.ui.models.UserUiModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val viewmodel by viewModels<ChatViewModel>()
    private var conversationUiModel: ConversationUiModel? = null
    private var userUiModel: UserUiModel? = null
    private lateinit var chatAdapter: ChatAdapter
    private var users = mutableListOf<UserUiModel>()
    private lateinit var binding: FragmentChatBinding
    private val args: ChatFragmentArgs by navArgs()
    private var shouldScroll = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentChatBinding.inflate(inflater, container, false)
        conversationUiModel = args.conversation
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        initRecyclerview()
        bindUI()
        setupObservers()
        setUpCustomItemTouchHelper()
        setupNavigationToUserFragment()
    }

    @SuppressLint("SetTextI18n")
    private fun setupObservers() {
        //Fetch users from the database
        viewmodel
            .getUsersInConversation(conversationUiModel?.conversationId!!)
            .observe(viewLifecycleOwner) {
                // TODO Make the view dumb and move any logic from here
                //update the global variable of users we need this to switch the users
                users = it as MutableList<UserUiModel>

                // When current user is null, make the first user in the users list as the current user

                userUiModel = it[0]
                chatAdapter.userUiModel = userUiModel

                viewmodel.updateUserUiModel(userUiModel as UserUiModel)

                //Now we can fetch the chats
                fetchChats()
            }

        //Observe changes made to the current user
        viewmodel.currentUser.observe(viewLifecycleOwner) { userUiModel ->
            when {
                userUiModel != null -> {
                    binding.activityChatTitle.text = getString(R.string.chat_chatting_as,userUiModel.name)
                }
                else -> {
                    binding.activityChatTitle.text = getString(R.string.chat_new_chat)
                }
            }
        }
    }

    private fun bindUI() {
        binding.apply {
            floatingActionButtonexchange.setOnClickListener {
                userUiModel = swapCurrentUser()
                viewmodel.updateUserUiModel(userUiModel = userUiModel as UserUiModel)
            }

            //navigate up
            imageViewback.setOnClickListener {
                it.findNavController().navigateUp()
            }

            sendTextButton.setOnClickListener {
                sendText()
            }
        }
    }

    private fun swapCurrentUser() = if (userUiModel == users[0]) users[1] else users[0]

    private fun initRecyclerview() {
        chatAdapter = ChatAdapter()

        val layout = LinearLayoutManager(context)

        binding.chatRecyclerView.apply {
            layout.reverseLayout = false
            layoutManager = layout
            adapter = chatAdapter
        }
    }

    private fun fetchChats() {
        viewmodel.getMessagesInConversation(conversationUiModel!!.conversationId!!)
            .observe(viewLifecycleOwner) {
                if (it.isNullOrEmpty()) {
                    binding.layoutNomessage.visibility = View.VISIBLE
                    binding.textViewInfoChats.visibility = View.GONE
                } else {
                    binding.layoutNomessage.visibility = View.GONE
                    binding.textViewInfoChats.visibility = View.VISIBLE

                    chatAdapter.submitList(it)

                    if (shouldScroll) {
                        scrollToLatestText()
                    }

                    chatAdapter.notifyDataSetChanged()
                }
            }
    }

    private fun sendText() {
        if (binding.messageEditText.text.toString().trim().isNotBlank()) {
            val text = binding.messageEditText.text.toString()

            val messageUiModel = MessageUiModel(
                userId = userUiModel?.userId,
                username = userUiModel?.name,
                message = text,
                timesent = System.currentTimeMillis(),
                conservationId = conversationUiModel?.conversationId
            )
            viewmodel.addMessage(messageUiModel)

            updateConversation(text)

            clearEditText()

            shouldScroll = true
        }
    }

    //Updates the details of this conversation we're having so we can display it on the previous page
    private fun updateConversation(text: String) {
        //we need last user, last message and the last time it was sent
        conversationUiModel?.userId = userUiModel?.userId
        conversationUiModel?.lastMessage = text
        conversationUiModel?.lasttimemessage = System.currentTimeMillis()
        conversationUiModel?.run {
            viewmodel.updateConversation(this)
        }
    }

    private fun clearEditText() {
        binding.messageEditText.setText("")
    }

    private fun scrollToLatestText() {
        // TODO Look into this unknown functionality
        //Some funky behaviour here !!!
        binding.chatRecyclerView.smoothScrollToPosition(
            chatAdapter.itemCount + 3
        )
        shouldScroll = false
    }

    private fun setUpCustomItemTouchHelper() {
        val itemTouchHelperCallback =
            object :
                ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
                ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                    // TODO Use UI Model
                    val messageEntity = chatAdapter.getItemAt(viewHolder.adapterPosition).messageEntity

                    // TODO Break down this if and nested when
                    if (users.size == 2) {
                        when (direction) {
                            ItemTouchHelper.LEFT -> {
                                messageEntity.userId = users[1].userId
                            }
                            ItemTouchHelper.RIGHT -> {
                                messageEntity.userId = users[0].userId
                            }
                        }
                    } else {
                        when (direction) {
                            ItemTouchHelper.LEFT -> {
                                messageEntity.userId = users[1].userId
                            }
                        }
                    }

                    viewmodel.updateMessage(messageEntity.toDomain().toUiModel())
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean
                ) {

                    RecyclerViewSwipeDecorator.Builder(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                        .create()
                        .decorate()

                    super.onChildDraw(
                        c,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )

                }
            }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.chatRecyclerView)
    }

    private fun setupNavigationToUserFragment() {
        binding.editUsers.setOnClickListener {
            val action =
                ChatFragmentDirections.chatToUsers(
                    conversation = conversationUiModel!!,
                    user = null
                )
            it.findNavController().navigate(action)
        }
    }
}
