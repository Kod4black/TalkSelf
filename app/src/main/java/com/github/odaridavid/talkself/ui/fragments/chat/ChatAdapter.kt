package com.github.odaridavid.talkself.ui.fragments.chat

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.data.room.relations.ChatAndUser
import com.github.odaridavid.talkself.databinding.ItemChatLeftBinding
import com.github.odaridavid.talkself.databinding.ItemChatRightBinding
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.utils.UtilityFunctions
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.bindImage

class ChatAdapter : ListAdapter<ChatAndUser, RecyclerView.ViewHolder>(
    ChatDiffUtil
) {

    var currentUser: User? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rightBinding =
            ItemChatRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (viewType == VIEW_TYPE_MESSAGE_RIGHT) {

            return RightChatHolder(rightBinding)

        } else if (viewType == VIEW_TYPE_MESSAGE_LEFT) {

            val leftBinding =
                ItemChatLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return LeftChatHolder(leftBinding)
        }

        return RightChatHolder(rightBinding)
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

    private class LeftChatHolder(val binding: ItemChatLeftBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(previousChatAndUser: ChatAndUser?, chatAndUser: ChatAndUser) {

            binding.apply {

                val date =
                    chatAndUser.chat.timesent?.let { UtilityFunctions.formatMillisecondsToDate(it) }
                val prevDate = previousChatAndUser?.chat?.timesent?.let {
                    UtilityFunctions.formatMillisecondsToDate(it)
                }

                textGchatMessageOther.text = chatAndUser.chat.message
                textGchatTimestampOther.text =
                    chatAndUser.chat.timesent?.let { UtilityFunctions.formatMillisecondsToTime(it) }
                textGchatDateOther.text = date?.split(",")?.first()

                textGchatUserOther.text = chatAndUser.user.name
                layoutGchatContainerOther.setBackgroundColor(Color.parseColor(chatAndUser.user.color))

                imageGchatProfileOther.apply {
                    context.bindImage(chatAndUser.user.imageUri!!, this)
                }

                if (previousChatAndUser != null && previousChatAndUser.chat.userId == chatAndUser.chat.userId) {
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

    private class RightChatHolder(val binding: ItemChatRightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(previousChatAndUser: ChatAndUser?, chatAndUser: ChatAndUser) {

            binding.apply {

                val date =
                    chatAndUser.chat.timesent?.let { UtilityFunctions.formatMillisecondsToDate(it) }
                val prevDate = previousChatAndUser?.chat?.timesent?.let {
                    UtilityFunctions.formatMillisecondsToDate(it)
                }

                textGchatMessageRight.text = chatAndUser.chat.message
                textGchatUserDate.text = date?.split(",")?.first()
                textGchatNameRight.text = chatAndUser.user.name
                textGchatTimestampMe.text =
                    chatAndUser.chat.timesent?.let { UtilityFunctions.formatMillisecondsToTime(it) }
                imageGchatProfileOther.apply {
                    context.bindImage(chatAndUser.user.imageUri!!, this)
                }

                layoutGchatContainerMe.setBackgroundColor(Color.parseColor(chatAndUser.user.color))


                if (previousChatAndUser != null && previousChatAndUser.chat.userId == chatAndUser.chat.userId) {
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
        return if (chatAndUser.chat.userId == currentUser?.userId) {
            // If the current user is the sender of the message
            VIEW_TYPE_MESSAGE_RIGHT
        } else {
            // If some other user sent the message
            VIEW_TYPE_MESSAGE_LEFT
        }
    }

    fun getItemAt(position: Int): ChatAndUser {
        return getItem(position)
    }

    companion object {
        val ChatDiffUtil = object : DiffUtil.ItemCallback<ChatAndUser>() {
            override fun areItemsTheSame(oldItem: ChatAndUser, newItem: ChatAndUser): Boolean {
                return oldItem.chat.chatId == newItem.chat.chatId && oldItem.chat.userId == newItem.chat.userId && oldItem.user.userId == newItem.user.userId
            }

            override fun areContentsTheSame(oldItem: ChatAndUser, newItem: ChatAndUser): Boolean {
                return oldItem == newItem
            }
        }

        private const val VIEW_TYPE_MESSAGE_RIGHT = 1
        private const val VIEW_TYPE_MESSAGE_LEFT = 2
    }
}
