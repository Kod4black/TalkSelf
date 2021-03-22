package com.github.odaridavid.talkself.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.ui.adapter.ConversationAdapter
import com.github.odaridavid.talkself.ui.viewmodel.ChatActivityViewModel
import com.github.odaridavid.talkself.ui.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewmodel by viewModels<MainActivityViewModel>()
    private lateinit var conversationadapter: ConversationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        conversationadapter = ConversationAdapter()

        viewmodel.conversation.observe(this,{
            conversationadapter.submitList(it)
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_prefs -> {
                Intent(this,SettingsActivity::class.java).apply {
                    startActivity(this)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun createConversation(view: View) {
        val conversation = Conversation(0,System.currentTimeMillis(),"",0)
        Intent(applicationContext,ChatActivity::class.java).also {
            it.putExtra("conversation", conversation)
            startActivity(it)
        }
    }
}