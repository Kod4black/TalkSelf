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
    private  var currentUser: User? = null
    private lateinit var chatAdapter: ChatAdapter
    private var users = mutableListOf<User>()

    private lateinit var binding : FragmentChatBinding
    val args: ChatFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)

        initVariables()
        initRecyclerview()

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_chat,container,false)

        binding.floatingActionButtonexchange.setOnClickListener {
            currentUser = if (currentUser == users[0]){
                users[1]
            }else{
                users[0]
            }
            viewmodel.currentuser.postValue(currentUser)
        }


        viewmodel.users(conversation?.id!!).observe(requireActivity(), {
            when {
                it.size < 2 -> {

                    val view = getView()

                    val firstView = view.findViewById<TextInputLayout>(R.id.first_username_view)
                    val secondView = view.findViewById<TextInputLayout>(R.id.second_username_view)

                    showSetUsernameDialog(view, firstView,secondView)

                }

                else -> {

                    users = it as MutableList<User>

                    when (currentUser) {
                        null -> {
                            currentUser = it[0]
                            chatAdapter.currentUser = currentUser
                        }
                    }
                    viewmodel.currentuser.postValue(currentUser)
                    initViewModel()
                }

            }
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

                    when(direction){

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

        binding.sendTextButton.setOnClickListener {
            sendText()
        }

        return  binding.root
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
            if (it.isNullOrEmpty()){
                binding.layoutNomessage.visibility = View.VISIBLE
                binding.textViewInfoChats.visibility = View.GONE
            }else{
                binding.layoutNomessage.visibility = View.GONE
                binding.textViewInfoChats.visibility = View.VISIBLE
                chatAdapter.submitList(it)
                scrollToLatestText(it)
            }
        })
    }


     override fun getView(): View {
        return layoutInflater.inflate(
            R.layout.dialog_input_usernames,
            activity?.window?.decorView?.findViewById(android.R.id.content),
            false
        )
    }


    fun sendText() {
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
            list.size -1
        )
    }

    private fun showSetUsernameDialog(
        view: View?,
        first: TextInputLayout,
        second: TextInputLayout
    ) {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.title_create_usernames))
            .setView(view)
            .setPositiveButton(getString(R.string.create), null)
            .setNegativeButton("Dismiss",null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            positiveButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.black
                )
            )

            negativeButton.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.black
                )
            )

            negativeButton.setOnClickListener {
                dialog.dismiss()
//                TODO()
//                finish()
            }

            positiveButton.setOnClickListener {
                val firstUsername = first.editText?.text.toString().trim()
                val secondUsername = second.editText?.text.toString().trim()
                if (firstUsername.isNotBlank() && secondUsername.isNotBlank()) {

                    viewmodel.makeconversation(conversation!!)
                    viewmodel.addUser(User(firstUsername, conversation?.id))
                    viewmodel.addUser(User(secondUsername, conversation?.id))

                    initViewModel()
                    dialog.dismiss()
                } else {
                    if (firstUsername.isBlank())
                        first.error = "Invalid Username.Should Contain Characters."
                    if (secondUsername.isBlank())
                        second.error = "Invalid Username.Should Contain Characters."
                }
            }
        }

        dialog.setOnCancelListener {
            dialog.cancel()
            //                TODO()
//                finish()
        }
        dialog.show()
    }


}