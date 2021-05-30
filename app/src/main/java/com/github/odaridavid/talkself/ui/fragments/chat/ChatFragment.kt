package com.github.odaridavid.talkself.ui.fragments.chat

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)

        initVariables()
        initRecyclerview()

        binding.floatingActionButtonexchange.setOnClickListener {

            currentUser = if (currentUser == users[0]) users[1] else users[0]
            viewmodel.currentuser.postValue(currentUser)

        }


        viewmodel.users(conversation?.id!!).observe(viewLifecycleOwner, {

            users = it as MutableList<User>

            when (currentUser) {
                null -> {
                    currentUser = it[0]
                    chatAdapter.currentUser = currentUser
                }
            }
            viewmodel.currentuser.postValue(currentUser)
            initViewModel()
        })


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


        binding.sendTextButton.setOnClickListener {
            sendText()
        }

        setUpCustomItemTouchHelper()
        toolBarCommonStuff()
        return binding.root
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

    private fun initViewModel() {
        viewmodel.chats(conversation!!.id!!).observe(viewLifecycleOwner, {
            if (it.isNullOrEmpty()) {
                binding.layoutNomessage.visibility = View.VISIBLE
                binding.textViewInfoChats.visibility = View.GONE
            } else {
                binding.layoutNomessage.visibility = View.GONE
                binding.textViewInfoChats.visibility = View.VISIBLE

                chatAdapter.submitList(it)

                if (shouldScroll){
                    scrollToLatestText()
                }
            }
        })
    }


    private fun sendText() {
        if (binding.messageEditText.text.toString().trim().isNotBlank()) {
            val text = binding.messageEditText.text.toString()
            val chat = Chat(
                currentUser?.id,
                currentUser?.name,
                text,
                System.currentTimeMillis(),
                conversation?.id
            )
            viewmodel.addText(chat)
            updateConversation(text)
            clearEditText()
            shouldScroll = true
        }
    }

    private fun updateConversation(text: String) {
        conversation?.lastUser = currentUser?.name
        conversation?.lastMessage = text
        conversation?.lasttimemessage = System.currentTimeMillis()
        viewmodel.updateConversation(conversation)
    }


    private fun clearEditText() {
        binding.messageEditText.setText("")
    }

    private fun scrollToLatestText() {
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

                    val chat = chatAdapter.getItemat(viewHolder.adapterPosition)

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
        binding.editUsers.setOnClickListener {
            val bundle = bundleOf("conversation" to conversation)
            it.findNavController().navigate(R.id.chat_to_users,bundle)
        }
    }


}