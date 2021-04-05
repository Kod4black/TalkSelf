package com.github.odaridavid.talkself.ui.activities

import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.ui.adapter.ConversationAdapter
import com.github.odaridavid.talkself.ui.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import kotlinx.android.synthetic.main.activity_conversations.*
import java.util.*

@AndroidEntryPoint
class ConversationsActivity : AppCompatActivity() {

    private  val viewmodel by viewModels<MainActivityViewModel>()
    private lateinit var conversationadapter: ConversationAdapter
    private lateinit var snackbar: Snackbar
    var isActionMode:Boolean = false
    var counter = 0
    var selectionList  = mutableListOf<Conversation>()
    var convoList  = mutableListOf<Conversation>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversations)
        setUpactionBar()

        //Setup the recyclerview holding chats
        setUpAdapter()

        viewmodel.conversation.observe(this,{
            if (it.isNullOrEmpty()){
                textView_Info_conversations.text = "You currently have no existing conversations"
                imageView_empty.visibility = View.VISIBLE
            }else {
                textView_Info_conversations.text = "Swipe a conversation Left or Right to delete it"
                imageView_empty.visibility = View.GONE
            }
            conversationadapter.submitList(it)
            convoList = it as MutableList<Conversation>
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
                    val conversation = conversationadapter.getItemat(viewHolder.adapterPosition)
                    showDeleteDialog(conversation)
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
        itemTouchHelper.attachToRecyclerView(conversations_recyclerView)

    }

    private fun setUpactionBar() {
        supportActionBar?.title = "Your Conversations";
//        supportActionBar?.title.col
    }

    private fun setUpAdapter() {
        conversationadapter = ConversationAdapter(this)
        conversations_recyclerView.layoutManager = LinearLayoutManager(this)
        conversations_recyclerView.adapter = conversationadapter
    }


    fun createConversation(view: View) {
        val conversation = Conversation(Random().nextInt(),System.currentTimeMillis())
        Intent(applicationContext,ChatActivity::class.java).also {
            it.putExtra("conversation", conversation)
            startActivity(it)
        }
    }

    fun showDeleteDialog(conversation: Conversation){
        val dialog = AlertDialog.Builder(this)
            .setTitle("Confirm Delete...")
            .setMessage("Are you sure you wanna delete this?")
            .setPositiveButton("Okay") { _, _ ->
                viewmodel.deleteConversation(conversation)
                showSnackBar("Conversation deleted", conversation)
            }
            .setNegativeButton("No") { dialog, _ ->
                viewmodel.deleteConversation(conversation)
                viewmodel.makeconversation(conversation)
                conversationadapter.notifyDataSetChanged()
                dialog.cancel() }
            .create()

        dialog.show()
    }

    fun showSnackBar(message: String, conversation: Conversation){
        snackbar = Snackbar.make(conversations_recyclerView,message,Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            viewmodel.makeconversation(conversation)
        }
        snackbar.show()
    }

    fun startselection(position: Int) {
        if (!isActionMode){
            isActionMode = true
            selectionList.add(convoList[position])
            counter++
            updateToolbar(counter)
        }
    }

    private fun updateToolbar(counter : Int) {

    }
}