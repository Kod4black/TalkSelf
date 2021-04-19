package com.github.odaridavid.talkself.ui.fragments.conversation

import android.content.Intent
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.databinding.FragmentConversationsBinding
import com.github.odaridavid.talkself.models.Conversation
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator
import java.util.*

@AndroidEntryPoint
class ConversationFragment: Fragment(R.layout.fragment_conversations) {

    private  val viewmodel by viewModels<ConversationsViewModel>()
    private lateinit var conversationadapter: ConversationAdapter
    private lateinit var snackbar: Snackbar
    var isActionMode:Boolean = false
    var counter = 0
    var selectionList  = mutableListOf<Conversation>()
    var conversationList  = mutableListOf<Conversation>()
    private val binding : FragmentConversationsBinding by lazy {
        DataBindingUtil.setContentView<FragmentConversationsBinding>(requireActivity(), R.layout.fragment_conversations).apply {
            lifecycleOwner = requireActivity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



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
                                requireContext(),
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
        itemTouchHelper.attachToRecyclerView(binding.conversationsRecyclerView)

    }


    private fun setUpAdapter() {
        conversationadapter = ConversationAdapter()
        binding.conversationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.conversationsRecyclerView.adapter = conversationadapter
    }


    fun createConversation(view: View) {
        val conversation = Conversation(Random().nextInt(),System.currentTimeMillis())
        Intent(requireContext(), ChatActivity::class.java).also {
            it.putExtra("conversation", conversation)
            startActivity(it)
        }
    }

    fun showDeleteDialog(conversation: Conversation){
        val dialog = AlertDialog.Builder(requireContext())
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
        snackbar = Snackbar.make(binding.conversationsRecyclerView,message,Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            viewmodel.makeconversation(conversation)
        }
        snackbar.show()
    }

}