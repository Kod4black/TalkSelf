package com.github.odaridavid.talkself.ui.conversation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.common.TimeUtils
import com.github.odaridavid.talkself.common.bindImage
import com.github.odaridavid.talkself.data.local.models.ConversationEntity
import com.github.odaridavid.talkself.data.local.models.UserEntity
import com.github.odaridavid.talkself.data.local.relations.ConversationAndUser
import com.github.odaridavid.talkself.databinding.ItemConvesationsBinding
import com.github.odaridavid.talkself.ui.models.ConversationUiModel

internal class ConversationAdapter(
    // TODO Passing lifecycle owner to adapter ,look into it
    private val lifecycleOwner: LifecycleOwner,
    // TODO Look into this state thing
    private val stateManager: ConversationsToolbarStateManager,
    private val onConversationClick: (ConversationEntity) -> Unit,
    private val onConversationLongClick: (ConversationEntity) -> Unit
) : ListAdapter<ConversationAndUser, ConversationAdapter.ViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemConvesationsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversationAndUser = getItem(position)

        // TODO Use UI models
        holder.bindConversation(conversationAndUser.conversationEntity)
        holder.bindUser(conversationAndUser.userEntity)

        holder.itemView.setOnClickListener {

            // TODO Move navigation logic to view + navigation class/controller file
            if (stateManager.isMultiSelectionStateActive()) {
                conversationAndUser.conversationEntity?.let { conversation ->
                    onConversationClick.invoke(conversation)
                }
            } else {
                val actions = conversationAndUser.conversationEntity?.let { conversation ->
                    ConversationFragmentDirections.conversationToChat(
                        ConversationUiModel(
                            conversationId = conversation.conversationId,
                            timeCreated = conversation.timeCreated,
                            userId = conversation.userId,
                            lastMessage = conversation.lastMessage,
                            lasttimemessage = conversation.lasttimemessage
                        )
                    )
                }
                if (actions != null) {
                    it.findNavController().navigate(actions)
                }
            }

        }

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

    inner class ViewHolder(
        val binding: ItemConvesationsBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindConversation(message: ConversationEntity?) {
            binding.apply {
                textChatLastMessage.text = message?.lastMessage
                conversationTime.text =
                    TimeUtils.formatMillisecondsToDate(createdAt = message?.timeCreated!!)
            }
        }

        fun bindUser(userEntity: UserEntity?) {
            binding.apply {
                conversationLastMan.text = userEntity?.name
                imageGchatProfileOther.apply {
                    bindImage(context = context, imageUrl = userEntity?.imageUri)
                }
            }
        }
    }

    fun getItemAt(position: Int): ConversationAndUser = getItem(position)

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<ConversationAndUser>() {
            override fun areItemsTheSame(
                oldItem: ConversationAndUser,
                newItem: ConversationAndUser
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ConversationAndUser,
                newItem: ConversationAndUser
            ): Boolean = oldItem.conversationEntity == newItem.conversationEntity &&
                    oldItem.userEntity == newItem.userEntity
        }
    }
}
