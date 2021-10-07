package com.github.odaridavid.talkself.ui.fragments.chat

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
import com.github.odaridavid.talkself.databinding.FragmentChatBinding
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator


@AndroidEntryPoint
class ChatFragment : Fragment() {

    private val viewmodel by viewModels<ChatViewModel>()
    private var conversation: Conversation? = null
    private var currentUser: User? = null
    private lateinit var chatAdapter: ChatAdapter
    private var users = mutableListOf<User>()

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
        conversation = args.conversation
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        initRecyclerview()
        bindUI()
        fireUpObservers()
        setUpCustomItemTouchHelper()
        toolBarCommonStuff()
    }

    @SuppressLint("SetTextI18n")
    private fun fireUpObservers() {
        //Fetch users from the database
        viewmodel.getUsersInConversation(conversation?.conversationId!!)
            .observe(viewLifecycleOwner, {

                //update the global variable of users we need this to switch the users
                users = it as MutableList<User>

                // When current user is null, make the first user in the users list as the current user

                currentUser = it[0]
                chatAdapter.currentUser = currentUser


                //Update the viewmodel also with the current user
                viewmodel.currentuser.postValue(currentUser)

                //Now we can fetch the chats
                fetchChats()
            })

        //Observe changes made to the current user
        viewmodel.currentuser.observe(viewLifecycleOwner, {
            when {
                it != null -> {
                    binding.activityChatTitle.text = "Chatting as ${it.name}."
                }
                else -> {
                    binding.activityChatTitle.text = "New Chat."
                }
            }
        })
    }

    private fun bindUI() {

        binding.apply {

            // switches the current user
            floatingActionButtonexchange.setOnClickListener {
                currentUser = if (currentUser == users[0]) users[1] else users[0]
                viewmodel.currentuser.postValue(currentUser)

            }

            //navigate up
            imageViewback.setOnClickListener {
                it.findNavController().navigateUp()
            }

            //send text
            sendTextButton.setOnClickListener {
                sendText()
            }

        }
    }


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
        viewmodel.getMessagesInConversation(conversation!!.conversationId!!)
            .observe(viewLifecycleOwner, {
                if (it.isNullOrEmpty()) {
                    //show the empty state
                    binding.layoutNomessage.visibility = View.VISIBLE
                    binding.textViewInfoChats.visibility = View.GONE
                } else {

                    //hide empty state and display chats
                    binding.layoutNomessage.visibility = View.GONE
                    binding.textViewInfoChats.visibility = View.VISIBLE


                    chatAdapter.submitList(it)

                    if (shouldScroll) {
                        scrollToLatestText()
                    }

                    chatAdapter.notifyDataSetChanged()

                }
            })
    }


    private fun sendText() {
        if (binding.messageEditText.text.toString().trim().isNotBlank()) {
            //get text
            val text = binding.messageEditText.text.toString()
            // make a new chat
            val chat = Chat(
                userId = currentUser?.userId,
                username = currentUser?.name,
                message = text,
                timesent = System.currentTimeMillis(),
                conservationId = conversation?.conversationId
            )
            //add it
            viewmodel.addMessage(chat)

            //update the conversation
            updateConversation(text)
            //clear edit text
            clearEditText()

            //should scroll to bottom
            shouldScroll = true
        }
    }

    //Updates the details of this conversation we're having so we can display it on the previous page
    private fun updateConversation(text: String) {

        //we need last user, last message and the last time it was sent
        conversation?.userId = currentUser?.userId
        conversation?.lastMessage = text
        conversation?.lasttimemessage = System.currentTimeMillis()
        conversation?.run {
            viewmodel.updateConversation(this)
        }
    }


    private fun clearEditText() {
        binding.messageEditText.setText("")
    }

    private fun scrollToLatestText() {

        //Some funky behaviour here !!!
        binding.chatRecyclerView.smoothScrollToPosition(
            chatAdapter.itemCount + 3
        )
        shouldScroll = false
    }

    // Just a custom item touch helper.
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

                    val chat = chatAdapter.getItemAt(viewHolder.adapterPosition).chat

                    if (users.size == 2) {
                        when (direction) {
                            ItemTouchHelper.LEFT -> {
                                chat.userId = users[1].userId
                            }
                            ItemTouchHelper.RIGHT -> {
                                chat.userId = users[0].userId
                            }
                        }
                    } else {
                        when (direction) {
                            ItemTouchHelper.LEFT -> {
                                chat.userId = users[1].userId
                            }
                        }
                    }

                    viewmodel.updatechat(chat)

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

    private fun toolBarCommonStuff() {

        //navigate to edit users
        binding.editUsers.setOnClickListener {
            //we need conversation id so we can use it to fetch users associated with this conversation.
            val action =
                ChatFragmentDirections.chatToUsers(conversation = conversation!!, user = null)
            it.findNavController().navigate(action)
        }
    }


}
