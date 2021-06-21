package com.github.odaridavid.talkself.ui.fragments.chat

import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.R
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

        initVariables()
        initRecyclerview()
        bindUI()
        fireUpObservers()
        setUpCustomItemTouchHelper()
        toolBarCommonStuff()
        return binding.root
    }

    private fun fireUpObservers() {
        //Fetch users from the database
        viewmodel.users(conversation?.id!!).observe(viewLifecycleOwner, {

            //update the global variable of users we need this to switch the users
            users = it as MutableList<User>

            // When current user is null, make the first user in the users list as the current user
            when (currentUser) {
                null -> {
                    currentUser = it[0]
                    chatAdapter.currentUser = currentUser
                }
            }

            //Update the viewmodel also with the current user
            viewmodel.currentuser.postValue(currentUser)

            //Now we can fetch the chats
            fetchChats()
        })

        //Observe changes made to the current user
        viewmodel.currentuser.observe(viewLifecycleOwner, {
            when {
                it != null -> {
                    binding.activityChatTitle.text = "Chatting as ${it.name}.";
                }
                else -> {
                    binding.activityChatTitle.text = "New Chat.";
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


    private fun initVariables() {
        conversation = args.conversation
    }


    private fun initRecyclerview() {

        chatAdapter = ChatAdapter()

        val layout = LinearLayoutManager(context)

        binding.chatRecyclerView.apply {
            layout.reverseLayout = false
            layoutManager = layout
            adapter = chatAdapter
            layout.stackFromEnd = true
        }

    }

    private fun fetchChats() {
        viewmodel.chats(conversation!!.id!!).observe(viewLifecycleOwner, {
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

            }
        })
    }


    private fun sendText() {
        if (binding.messageEditText.text.toString().trim().isNotBlank()) {
            //get text
            val text = binding.messageEditText.text.toString()
            // make a new chat
            val chat = Chat(
                currentUser?.id,
                currentUser?.name,
                text,
                System.currentTimeMillis(),
                conversation?.id
            )
            //add it
            viewmodel.addText(chat)

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
        conversation?.lastUser = currentUser?.name
        conversation?.lastMessage = text
        conversation?.lasttimemessage = System.currentTimeMillis()
        viewmodel.updateConversation(conversation)
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

                    val chat = chatAdapter.getItemAt(viewHolder.adapterPosition)

                    when (direction) {

                        ItemTouchHelper.LEFT -> {
                            chat.userid = users[1].id
                            chat.username = users[1].name
                        }

                        ItemTouchHelper.RIGHT -> {
                            chat.userid = users[0].id
                            chat.username = users[0].name
                        }

                    }
                    viewmodel.updatechat(chat)
                    chatAdapter.notifyDataSetChanged()
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
            val bundle = bundleOf("conversation" to conversation)
            //we need conversation id so we can use it to fetch users associated with this conversation.
            it.findNavController().navigate(R.id.chat_to_users, bundle)
        }
    }


}