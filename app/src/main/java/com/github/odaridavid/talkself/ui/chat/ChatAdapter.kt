package com.github.odaridavid.talkself.ui.chat

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.common.TimeUtils
import com.github.odaridavid.talkself.common.bindImage
import com.github.odaridavid.talkself.data.local.relations.MessageAndUser
import com.github.odaridavid.talkself.databinding.ItemChatLeftBinding
import com.github.odaridavid.talkself.databinding.ItemChatRightBinding
import com.github.odaridavid.talkself.ui.models.UserUiModel

internal class ChatAdapter : ListAdapter<MessageAndUser, RecyclerView.ViewHolder>(ChatDiffUtil) {

    var userUiModel: UserUiModel? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        if (viewType == VIEW_TYPE_MESSAGE_RIGHT) {
            val rightBinding = ItemChatRightBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            RightChatHolder(rightBinding)
        } else {
            val leftBinding = ItemChatLeftBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            LeftChatHolder(leftBinding)
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val chatAndUser = getItem(position)

        val previousChatAndUser = if (position >= 1) getItem(position - 1) else null

        when (holder.itemViewType) {

            VIEW_TYPE_MESSAGE_RIGHT -> (holder as RightChatHolder).bind(
                previousChatAndUser,
                chatAndUser
            )

            VIEW_TYPE_MESSAGE_LEFT -> (holder as LeftChatHolder).bind(
                previousChatAndUser,
                chatAndUser
            )
        }
    }

    private inner class LeftChatHolder(val binding: ItemChatLeftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(previousMessageAndUser: MessageAndUser?, messageAndUser: MessageAndUser) {

            binding.apply {

                val date =
                    messageAndUser.messageEntity.timesent?.let { createdAt ->
                        TimeUtils.formatMillisecondsToDate(createdAt = createdAt)
                    }
                val prevDate = previousMessageAndUser?.messageEntity?.timesent?.let { createdAt ->
                    TimeUtils.formatMillisecondsToDate(createdAt = createdAt)
                }

                textGchatMessageOther.text = messageAndUser.messageEntity.message
                textGchatTimestampOther.text =
                    messageAndUser.messageEntity.timesent?.let { createdAt ->
                        TimeUtils.formatMillisecondsToHrsAndMinutes(createdAt = createdAt)
                    }
                textGchatDateOther.text = date?.split(",")?.first()

                textGchatUserOther.text = messageAndUser.userEntity.name
                layoutGchatContainerOther.setBackgroundColor(Color.parseColor(messageAndUser.userEntity.color))

                imageGchatProfileOther.apply {
                    bindImage(context = context, imageUrl = messageAndUser.userEntity.imageUri!!)
                }

                if (previousMessageAndUser != null && previousMessageAndUser.messageEntity.userId == messageAndUser.messageEntity.userId) {
                    textGchatUserOther.visibility = View.GONE
                    imageGchatProfileOther.visibility = View.GONE

                } else {

                    textGchatUserOther.visibility = View.VISIBLE
                    imageGchatProfileOther.visibility = View.VISIBLE

                }

                when {
                    previousMessageAndUser == null -> {
                        textGchatDateOther.visibility = View.VISIBLE
                        return
                    }
                    date?.split(",")?.first() == prevDate?.split(",")?.first() -> {
                        textGchatDateOther.visibility = View.GONE
                    }
                    else -> {
                        textGchatDateOther.visibility = View.VISIBLE
                    }
                }

            }

        }

    }

    private inner class RightChatHolder(val binding: ItemChatRightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(previousMessageAndUser: MessageAndUser?, messageAndUser: MessageAndUser) {

            binding.apply {

                val date =
                    messageAndUser.messageEntity.timesent?.let { createdAt ->
                        TimeUtils.formatMillisecondsToDate(createdAt = createdAt)
                    }
                val prevDate = previousMessageAndUser?.messageEntity?.timesent?.let { createdAt ->
                    TimeUtils.formatMillisecondsToDate(createdAt = createdAt)
                }

                textGchatMessageRight.text = messageAndUser.messageEntity.message
                textGchatUserDate.text = date?.split(",")?.first()
                textGchatNameRight.text = messageAndUser.userEntity.name
                textGchatTimestampMe.text =
                    messageAndUser.messageEntity.timesent?.let { createdAt ->
                        TimeUtils.formatMillisecondsToHrsAndMinutes(createdAt)
                    }
                imageGchatProfileOther.apply {
                    bindImage(context = context, imageUrl = messageAndUser.userEntity.imageUri!!)
                }

                layoutGchatContainerMe.setBackgroundColor(Color.parseColor(messageAndUser.userEntity.color))


                if (previousMessageAndUser != null && previousMessageAndUser.messageEntity.userId == messageAndUser.messageEntity.userId) {
                    textGchatNameRight.visibility = View.GONE
                    imageGchatProfileOther.visibility = View.GONE
                } else {
                    textGchatNameRight.visibility = View.VISIBLE
                    imageGchatProfileOther.visibility = View.VISIBLE
                }

                when {
                    previousMessageAndUser == null -> {
                        textGchatUserDate.visibility = View.VISIBLE
                        return
                    }
                    date?.split(",")?.first() == prevDate?.split(",")?.first() -> {
                        textGchatUserDate.visibility = View.GONE
                    }
                    else -> {
                        textGchatUserDate.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val chatAndUser = getItem(position)
        return if (chatAndUser.messageEntity.userId == userUiModel?.userId) {
            VIEW_TYPE_MESSAGE_RIGHT
        } else VIEW_TYPE_MESSAGE_LEFT
    }

    fun getItemAt(position: Int): MessageAndUser = getItem(position)

    companion object {
        val ChatDiffUtil = object : DiffUtil.ItemCallback<MessageAndUser>() {
            override fun areItemsTheSame(oldItem: MessageAndUser, newItem: MessageAndUser): Boolean {
                return oldItem.messageEntity.chatId == newItem.messageEntity.chatId && oldItem.messageEntity.userId == newItem.messageEntity.userId && oldItem.userEntity.userId == newItem.userEntity.userId
            }

            override fun areContentsTheSame(oldItem: MessageAndUser, newItem: MessageAndUser): Boolean {
                return oldItem == newItem
            }
        }

        private const val VIEW_TYPE_MESSAGE_RIGHT = 1
        private const val VIEW_TYPE_MESSAGE_LEFT = 2
    }
}
