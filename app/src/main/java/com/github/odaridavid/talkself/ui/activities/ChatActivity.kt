package com.github.odaridavid.talkself.ui.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.odaridavid.talkself.*
import com.github.odaridavid.talkself.models.Chat
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

    private lateinit var userOne: User
    private lateinit var userTwo: User
    private lateinit var currentUser: User

    private var userOneId: Int = INVALID_DEFAULT_ID
    private var userTwoId: Int = INVALID_DEFAULT_ID

    private var userOneName: String = DEFAULT_USERNAME
    private var userTwoName: String = DEFAULT_USERNAME


    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val sharedPreference = (application as TalkSelfApp).sharedPref

        initUserValues(sharedPreference)

        if (userOneName.contains(DEFAULT_USERNAME)) {
            val view = getView()

            val firstView = view.findViewById<TextInputLayout>(R.id.first_username_view)
            val secondView = view.findViewById<TextInputLayout>(R.id.second_username_view)

            showSetUsernameDialog(view, firstView, secondView, sharedPreference)
        } else {
            initUsers()
            initViewModel()
        }
        adapter = ChatAdapter()
        chat_recycler_view.adapter = adapter

        floatingActionButtonexchange.setOnClickListener {
            currentUser = if (currentUser.id == userOne.id) userTwo else userOne
            adapter.currentUser = currentUser
            adapter.notifyDataSetChanged()
            clearEditText()
            scrollToLatestText()
        }

    }

    private fun initViewModel() {
        viewmodel.chatList.observe(this, {
            adapter.currentUser = currentUser
            adapter.submitList(it)
        })
    }

    private fun initUsers() {
        userOne = User(userOneId, userOneName)
        userTwo = User(userTwoId, userTwoName)
        //Default
        currentUser = userOne
    }

    private fun getView(): View {
        return layoutInflater.inflate(
            R.layout.dialog_input_usernames,
            window.decorView.findViewById(android.R.id.content),
            false
        )
    }

    private fun initUserValues(sharedPreference: SharedPreferences) {
        userOneId = sharedPreference.getInt(USER_ONE_ID_PREF_KEY, INVALID_DEFAULT_ID)
        userTwoId = sharedPreference.getInt(USER_TWO_ID_PREF_KEY, INVALID_DEFAULT_ID)

        userOneName = sharedPreference.getString(USER_ONE_NAME_PREF_KEY, DEFAULT_USERNAME)!!
        userTwoName = sharedPreference.getString(USER_TWO_NAME_PREF_KEY, DEFAULT_USERNAME)!!
    }

    private fun showSetUsernameDialog(
        view: View?,
        first: TextInputLayout,
        second: TextInputLayout,
        sharedPreference: SharedPreferences
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
                val firstUsername = first.editText?.text.toString().trim()
                val secondUsername = second.editText?.text.toString().trim()
                if (firstUsername.isNotBlank() && secondUsername.isNotBlank()) {
                    val editor = sharedPreference.edit()
                    with(editor) {
                        putString(USER_ONE_NAME_PREF_KEY, firstUsername)
                        putString(USER_TWO_NAME_PREF_KEY, secondUsername)
                        apply()
                    }
                    initUserValues(sharedPreference)
                    initUsers()
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
                (0..Long.MAX_VALUE).random(),
                currentUser.id,
                currentUser.name,
                text,
                System.currentTimeMillis()
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