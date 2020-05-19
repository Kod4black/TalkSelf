package com.github.odaridavid.talkself

import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userOne: User
    private lateinit var userTwo: User
    private lateinit var currentUser: User

    private var userOneId: Int = INVALID_DEFAULT_ID
    private var userTwoId: Int = INVALID_DEFAULT_ID

    private var userOneName: String = DEFAULT_USERNAME
    private var userTwoName: String = DEFAULT_USERNAME

    private val chatList: MutableList<Chat> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreference = (application as TalkSelfApp).sharedPref

        initUserValues(sharedPreference)

        if (userOneName.contains(DEFAULT_USERNAME)) {
            val view = getView()

            val firstView = view.findViewById<TextInputLayout>(R.id.first_username_view)
            val secondView = view.findViewById<TextInputLayout>(R.id.second_username_view)

            showSetUsernameDialog(view, firstView, secondView, sharedPreference)
        } else {
            initUsers()
            setAdapter()
        }
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
        val dialog = AlertDialog.Builder(this@MainActivity)
            .setTitle(getString(R.string.title_create_usernames))
            .setView(view)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.create), null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            positiveButton.setTextColor(
                ContextCompat.getColor(
                    this@MainActivity,
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
                    setAdapter()
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


    private fun setAdapter() {
        chat_recycler_view.adapter = ChatAdapter(currentUser).apply {
            submitList(chatList)
        }
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
            chatList.add(chat)
            clearEditText()
            setAdapter()
            scrollToLatestText()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_user -> {
                currentUser = if (currentUser.id == userOne.id) userTwo else userOne
                clearEditText()
                setAdapter()
                scrollToLatestText()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun clearEditText() {
        message_edit_text.setText("")
    }

    private fun scrollToLatestText() {
        chat_recycler_view.layoutManager?.scrollToPosition(chatList.size - 1)
    }
}