package com.github.odaridavid.talkself.ui.fragments.conversation;

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.databinding.ItemConvesationsBinding
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.utils.UtilityFunctions

class ConversationAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val stateManager: ConversationsToolbarStateManager,
    private val onclick: (Conversation) -> Unit,
    private val onLongClick: (Conversation) -> Unit
) : ListAdapter<Conversation, ConversationAdapter.ViewHolder>(
    ConversationDiffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemConvesationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversation = getItem(position)
        holder.bind(conversation)

        holder.itemView.setOnClickListener {

            if (stateManager.isMultiSelectionStateActive()) {
                onclick.invoke(conversation)
            } else {
                val actions = ConversationFragmentDirections.conversationToChat(conversation)
                it.findNavController().navigate(actions)
            }

        }

        holder.binding.cardview.setOnLongClickListener {
            onLongClick.invoke(conversation)
            holder.binding.imageViewIsChecked.isVisible = true
            return@setOnLongClickListener true
        }



        stateManager.selectedConversations.observe(lifecycleOwner, {
            when {
                it.contains(conversation) -> {
                    holder.binding.imageViewIsChecked.isVisible = true
                }
                else -> {
                    holder.binding.imageViewIsChecked.isVisible = false
                }
            }
        })

    }

    class ViewHolder(val binding: ItemConvesationsBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(message: Conversation) {
            binding.textChatLastMessage.text = message.lastMessage
            binding.conversationLastMan.text = message.lastUser
            binding.conversationTime.text =
                UtilityFunctions.formatMillisecondsToDate(message.timeCreated!!)

        }

    }

    fun getItemAt(position: Int): Conversation {
        return getItem(position)
    }

    companion object {
        val ConversationDiffUtil = object : DiffUtil.ItemCallback<Conversation>() {
            override fun areItemsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Conversation, newItem: Conversation): Boolean {
                return oldItem == newItem
            }

        }
    }

    private fun selectionStateImageView(imageViewIsChecked: ImageView, isVisible: Boolean) {
        imageViewIsChecked.isVisible = isVisible
    }


}