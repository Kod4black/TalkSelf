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
import com.github.odaridavid.talkself.data.local.relations.ChatAndUser
import com.github.odaridavid.talkself.databinding.ItemChatLeftBinding
import com.github.odaridavid.talkself.databinding.ItemChatRightBinding
import com.github.odaridavid.talkself.ui.models.UserUiModel

internal class ChatAdapter : ListAdapter<ChatAndUser, RecyclerView.ViewHolder>(ChatDiffUtil) {

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

        fun bind(previousChatAndUser: ChatAndUser?, chatAndUser: ChatAndUser) {

            binding.apply {

                val date =
                    chatAndUser.chatEntity.timesent?.let { createdAt ->
                        TimeUtils.formatMillisecondsToDate(createdAt = createdAt)
                    }
                val prevDate = previousChatAndUser?.chatEntity?.timesent?.let { createdAt ->
                    TimeUtils.formatMillisecondsToDate(createdAt = createdAt)
                }

                textGchatMessageOther.text = chatAndUser.chatEntity.message
                textGchatTimestampOther.text =
                    chatAndUser.chatEntity.timesent?.let { createdAt ->
                        TimeUtils.formatMillisecondsToHrsAndMinutes(createdAt = createdAt)
                    }
                textGchatDateOther.text = date?.split(",")?.first()

                textGchatUserOther.text = chatAndUser.userEntity.name
                layoutGchatContainerOther.setBackgroundColor(Color.parseColor(chatAndUser.userEntity.color))

                imageGchatProfileOther.apply {
                    bindImage(context = context, imageUrl = chatAndUser.userEntity.imageUri!!)
                }

                if (previousChatAndUser != null && previousChatAndUser.chatEntity.userId == chatAndUser.chatEntity.userId) {
                    textGchatUserOther.visibility = View.GONE
                    imageGchatProfileOther.visibility = View.GONE

                } else {

                    textGchatUserOther.visibility = View.VISIBLE
                    imageGchatProfileOther.visibility = View.VISIBLE

                }

                when {
                    previousChatAndUser == null -> {
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

        fun bind(previousChatAndUser: ChatAndUser?, chatAndUser: ChatAndUser) {

            binding.apply {

                val date =
                    chatAndUser.chatEntity.timesent?.let { createdAt ->
                        TimeUtils.formatMillisecondsToDate(createdAt = createdAt)
                    }
                val prevDate = previousChatAndUser?.chatEntity?.timesent?.let { createdAt ->
                    TimeUtils.formatMillisecondsToDate(createdAt = createdAt)
                }

                textGchatMessageRight.text = chatAndUser.chatEntity.message
                textGchatUserDate.text = date?.split(",")?.first()
                textGchatNameRight.text = chatAndUser.userEntity.name
                textGchatTimestampMe.text =
                    chatAndUser.chatEntity.timesent?.let { createdAt ->
                        TimeUtils.formatMillisecondsToHrsAndMinutes(createdAt)
                    }
                imageGchatProfileOther.apply {
                    bindImage(context = context, imageUrl = chatAndUser.userEntity.imageUri!!)
                }

                layoutGchatContainerMe.setBackgroundColor(Color.parseColor(chatAndUser.userEntity.color))


                if (previousChatAndUser != null && previousChatAndUser.chatEntity.userId == chatAndUser.chatEntity.userId) {
                    textGchatNameRight.visibility = View.GONE
                    imageGchatProfileOther.visibility = View.GONE
                } else {
                    textGchatNameRight.visibility = View.VISIBLE
                    imageGchatProfileOther.visibility = View.VISIBLE
                }

                when {
                    previousChatAndUser == null -> {
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
        return if (chatAndUser.chatEntity.userId == userUiModel?.userId) {
            VIEW_TYPE_MESSAGE_RIGHT
        } else VIEW_TYPE_MESSAGE_LEFT
    }

    fun getItemAt(position: Int): ChatAndUser = getItem(position)

    companion object {
        val ChatDiffUtil = object : DiffUtil.ItemCallback<ChatAndUser>() {
            override fun areItemsTheSame(oldItem: ChatAndUser, newItem: ChatAndUser): Boolean {
                return oldItem.chatEntity.chatId == newItem.chatEntity.chatId && oldItem.chatEntity.userId == newItem.chatEntity.userId && oldItem.userEntity.userId == newItem.userEntity.userId
            }

            override fun areContentsTheSame(oldItem: ChatAndUser, newItem: ChatAndUser): Boolean {
                return oldItem == newItem
            }
        }

        private const val VIEW_TYPE_MESSAGE_RIGHT = 1
        private const val VIEW_TYPE_MESSAGE_LEFT = 2
    }
}
