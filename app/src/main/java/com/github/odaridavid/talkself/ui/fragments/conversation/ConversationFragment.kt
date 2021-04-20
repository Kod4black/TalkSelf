package com.github.odaridavid.talkself.ui.fragments.conversation

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
import androidx.navigation.findNavController
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
class ConversationFragment: Fragment() {

    private  val viewmodel by viewModels<ConversationsViewModel>()
    private lateinit var conversationadapter: ConversationAdapter
    private lateinit var snackbar: Snackbar
    var isActionMode:Boolean = false
    var counter = 0
    var selectionList  = mutableListOf<Conversation>()
    var conversationList  = mutableListOf<Conversation>()

    private lateinit var binding : FragmentConversationsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_conversations,container,false)

        setUpAdapter()

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

        viewmodel.conversation.observe(requireActivity(),{
            conversationadapter.submitList(it)
        })

        binding.floatingActionButtonNewConvo.setOnClickListener {
            val conversation = Conversation(Random().nextInt(),System.currentTimeMillis())
            val actions = ConversationFragmentDirections.actionConversationFragmentToChatFragment(conversation)

            it.findNavController().navigate(actions)
        }

        return  binding.root
    }


    private fun setUpAdapter() {
        conversationadapter = ConversationAdapter()
        binding.conversationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.conversationsRecyclerView.adapter = conversationadapter
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