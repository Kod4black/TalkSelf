package com.github.odaridavid.talkself.ui.fragments.chat

import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.databinding.FragmentChatBinding
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.google.android.material.textfield.TextInputLayout
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)

        initVariables()
        initRecyclerview()

        binding.floatingActionButtonexchange.setOnClickListener {
            currentUser = if (currentUser == users[0]) {
                users[1]
            } else {
                users[0]
            }
            viewmodel.currentuser.postValue(currentUser)
        }


        viewmodel.users(conversation?.id!!).observe(requireActivity(), {

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


        viewmodel.currentuser.observe(requireActivity(), {
            when {
                it != null -> {
                    binding.activityChatTitle.text = "Chatting as ${it.name}.";
                }
                else -> {
                    binding.activityChatTitle.text = "New Chat.";
                }
            }
        })

        setUpCustomItemTouchHelper()

        binding.sendTextButton.setOnClickListener {
            sendText()
        }

        return binding.root
    }


    private fun initVariables() {
        conversation = args.conversation
    }


    private fun initRecyclerview() {
        chatAdapter = ChatAdapter()
        binding.chatRecyclerView.adapter = chatAdapter
    }

    private fun initViewModel() {
        viewmodel.chats(conversation!!.id!!).observe(requireActivity(), {
            if (it.isNullOrEmpty()) {
                binding.layoutNomessage.visibility = View.VISIBLE
                binding.textViewInfoChats.visibility = View.GONE
            } else {
                binding.layoutNomessage.visibility = View.GONE
                binding.textViewInfoChats.visibility = View.VISIBLE
                chatAdapter.submitList(it)
                scrollToLatestText(it)
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

    private fun scrollToLatestText(list: List<Chat>) {
        binding.chatRecyclerView.layoutManager?.scrollToPosition(
            list.size - 1
        )
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

}