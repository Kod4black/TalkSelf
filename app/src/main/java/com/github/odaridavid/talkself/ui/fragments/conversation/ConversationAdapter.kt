package com.github.odaridavid.talkself.ui.fragments.conversation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.data.room.relations.ConversationAndUser
import com.github.odaridavid.talkself.databinding.ItemConvesationsBinding
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.utils.UtilityFunctions
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.bindImage

class ConversationAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val stateManager: ConversationsToolbarStateManager,
    private val onclick: (Conversation) -> Unit,
    private val onLongClick: (Conversation) -> Unit
) : ListAdapter<ConversationAndUser, ConversationAdapter.ViewHolder>(
    ConversationDiffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemConvesationsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversationAndUser = getItem(position)

        holder.bindConversation(conversationAndUser.conversation)
        holder.bindUser(conversationAndUser.user)

        holder.itemView.setOnClickListener {

            if (stateManager.isMultiSelectionStateActive()) {
                conversationAndUser.conversation?.let { it1 -> onclick.invoke(it1) }
            } else {
                val actions = conversationAndUser.conversation?.let { it1 ->
                    ConversationFragmentDirections.conversationToChat(
                        it1
                    )
                }
                if (actions != null) {
                    it.findNavController().navigate(actions)
                }
            }

        }

//        holder.binding.cardview.setOnLongClickListener {
//            conversationAndUser.conversation?.let { it1 -> onLongClick.invoke(it1) }
//            holder.binding.imageViewIsChecked.isVisible = true
//            return@setOnLongClickListener true
//        }



        stateManager.selectedConversations.observe(lifecycleOwner, {
            when {
                it.contains(conversationAndUser) -> {
                    holder.binding.imageViewIsChecked.isVisible = true
                }
                else -> {
                    holder.binding.imageViewIsChecked.isVisible = false
                }
            }
        })

    }

    class ViewHolder(val binding: ItemConvesationsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bindConversation(message: Conversation?) {
            binding.apply {
                textChatLastMessage.text = message?.lastMessage
                conversationTime.text =
                    UtilityFunctions.formatMillisecondsToDate(message?.timeCreated!!)

            }

        }

        fun bindUser(user: User?) {
            binding.apply {
                conversationLastMan.text = user?.name
                imageGchatProfileOther.apply {
                    context.bindImage(user?.imageUri, this)
                }

            }
        }

    }

    fun getItemAt(position: Int): ConversationAndUser {
        return getItem(position)
    }

    companion object {
        val ConversationDiffUtil = object : DiffUtil.ItemCallback<ConversationAndUser>() {
            override fun areItemsTheSame(
                oldItem: ConversationAndUser,
                newItem: ConversationAndUser
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ConversationAndUser,
                newItem: ConversationAndUser
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


}