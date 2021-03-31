package com.github.odaridavid.talkself.ui.activities

import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.ui.adapter.ConversationAdapter
import com.github.odaridavid.talkself.ui.viewmodel.ChatActivityViewModel
import com.github.odaridavid.talkself.ui.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewmodel by viewModels<MainActivityViewModel>()
    private lateinit var conversationadapter: ConversationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpactionBar()

        //Setup the recyclerview holding chats
        setUpAdapter()

        viewmodel.conversation.observe(this,{
            if (it.isNullOrEmpty()){
                textView_Info.text = "You currently have no existing conversations"
            }else {
                textView_Info.text = "Swipe a conversation Left or Right to delete it"
            }
            conversationadapter.submitList(it)
        })

        //A custom itemtouchhelper to add a background to a viewholder
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
                   viewmodel.deleteConversation(conversationadapter.getItemat(viewHolder.adapterPosition))
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
                        .addBackgroundColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.colorAccent
                            )
                        )
                        .addActionIcon(R.drawable.ic_round_delete)
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
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    private fun setUpactionBar() {
        supportActionBar?.title = "Your Conversations";
//        supportActionBar?.title.col
    }

    private fun setUpAdapter() {
        conversationadapter = ConversationAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = conversationadapter
    }


    fun createConversation(view: View) {
        val conversation = Conversation(Random().nextInt(),System.currentTimeMillis())
        Intent(applicationContext,ChatActivity::class.java).also {
            it.putExtra("conversation", conversation)
            startActivity(it)
        }
    }
}