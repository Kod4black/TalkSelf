package com.github.odaridavid.talkself.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.odaridavid.talkself.*
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.ui.adapter.ChatAdapter
import com.github.odaridavid.talkself.ui.viewmodel.ChatActivityViewModel
import com.github.odaridavid.talkself.utils.*
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_chat.*
import java.util.*

@AndroidEntryPoint
class ChatActivity : AppCompatActivity() {

    private val viewmodel by viewModels<ChatActivityViewModel>()

    var conversation: Conversation? = null

    private  var currentUser: User? = null

    private lateinit var adapter: ChatAdapter

    private var users = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        conversation = intent.getParcelableExtra<Conversation>("conversation")

        adapter = ChatAdapter()
        chat_recycler_view.adapter = adapter

        floatingActionButtonexchange.setOnClickListener {
            if (currentUser == users[0]){
                currentUser = users[1]
            }else{
                currentUser = users[0]
            }
            viewmodel.currentuser.postValue(currentUser!!.name)
        }


        viewmodel.users(conversation?.id!!).observe(this, {
            if (it.size < 2){
                val view = getView()

                val firstView = view.findViewById<TextInputLayout>(R.id.first_username_view)
                val secondView = view.findViewById<TextInputLayout>(R.id.second_username_view)

                showSetUsernameDialog(view, firstView,secondView)

            }else{
                users = it as MutableList<User>
                if (currentUser == null){
                    currentUser = it[0]
                    adapter.currentUser = currentUser
                }
                viewmodel.currentuser.postValue(currentUser?.name)
                initViewModel()
            }
        })

        viewmodel.chats(conversation!!.id!!).observe(this, {
            adapter.submitList(it)
        })

        viewmodel.currentuser.observe(this, {
            if (it.isNullOrBlank()){
                supportActionBar?.title = "New Chat";
            }
            supportActionBar?.title = "Chatting as $it";
        })

    }

    private fun initViewModel() {
        viewmodel.chats(conversation!!.id!!).observe(this, {
            adapter.submitList(it)
        })
    }


    private fun getView(): View {
        return layoutInflater.inflate(
            R.layout.dialog_input_usernames,
            window.decorView.findViewById(android.R.id.content),
            false
        )
    }


    private fun showSetUsernameDialog(
        view: View?,
        first: TextInputLayout,
        second: TextInputLayout
    ) {
        val dialog = AlertDialog.Builder(this@ChatActivity)
            .setTitle(getString(R.string.title_create_usernames))
            .setView(view)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.create), null)
            .setNegativeButton("Dismiss",null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            positiveButton.setTextColor(
                ContextCompat.getColor(
                    this@ChatActivity,
                    android.R.color.black
                )
            )

            negativeButton.setTextColor(
                ContextCompat.getColor(
                    this@ChatActivity,
                    android.R.color.black
                )
            )

            negativeButton.setOnClickListener {
                dialog.dismiss()
                finish()
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
                        first.error = "Invalid Username.Should Contain Characters"
                    if (secondUsername.isBlank())
                        second.error = "Invalid Username.Should Contain Characters"
                }
            }
        }
        dialog.show()
    }


    fun sendText(view: View) {
        if (message_edit_text.text.toString().trim().isNotBlank()) {
            val text = message_edit_text.text.toString()
            val chat = Chat(
                currentUser?.id,
                currentUser?.name,
                text,
                System.currentTimeMillis(),
                conversation?.id
            )
            viewmodel.addText(chat)
            conversation?.lastUser = currentUser?.name
            conversation?.lastMessage = text
            conversation?.lasttimemessage = System.currentTimeMillis()
            viewmodel.updateConversation(conversation)
            clearEditText()
            scrollToLatestText()
        }
    }


    private fun clearEditText() {
        message_edit_text.setText("")
    }

    private fun scrollToLatestText() {
        viewmodel.chatList.value?.size?.minus(1)?.let {
            chat_recycler_view.layoutManager?.scrollToPosition(
                it
            )
        }
    }

    companion object {
        const val CHATS_KEY = "chats"
    }

}