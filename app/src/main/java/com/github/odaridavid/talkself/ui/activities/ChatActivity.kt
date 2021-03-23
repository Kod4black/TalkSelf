package com.github.odaridavid.talkself.ui.activities

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        conversation = intent.getParcelableExtra<Conversation>("conversation")

        adapter = ChatAdapter()
        chat_recycler_view.adapter = adapter

        floatingActionButtonexchange.setOnClickListener {
            refresh()
        }


        viewmodel.users(conversation?.id!!).observe(this, {
            if (it.size < 2){
                val view = getView()

                val firstView = view.findViewById<TextInputLayout>(R.id.first_username_view)

                showSetUsernameDialog(view, firstView)
            }else{
                if (currentUser == null){
                    currentUser = it[0]
                }
                initViewModel()
            }
        })

        viewmodel.chats(conversation!!.id!!).observe(this, {
            if (currentUser != null){
                adapter.currentUser = currentUser
            }
            adapter.submitList(it)
        })
    }

    private fun refresh() {
        adapter.notifyDataSetChanged()
        clearEditText()
        scrollToLatestText()
    }

    private fun initViewModel() {
        viewmodel.chats(conversation!!.id!!).observe(this, {
            if (currentUser != null){
                adapter.currentUser = currentUser
            }
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
    ) {
        val dialog = AlertDialog.Builder(this@ChatActivity)
            .setTitle(getString(R.string.title_create_usernames))
            .setView(view)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.create), null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)

            positiveButton.setTextColor(
                ContextCompat.getColor(
                    this@ChatActivity,
                    android.R.color.black
                )
            )

            positiveButton.setOnClickListener {
                val Username = first.editText?.text.toString().trim()
                if (Username.isNotBlank()) {
                    var user = User(Username, conversation?.id)
                    viewmodel.addUser(user)
                    initViewModel()
                    dialog.dismiss()
                } else {
                    first.error = "Invalid Username.Should Contain Characters"
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