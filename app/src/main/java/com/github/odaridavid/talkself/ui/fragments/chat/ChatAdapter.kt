 package com.github.odaridavid.talkself.ui.fragments.chat;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.databinding.ItemChatLeftBinding
import com.github.odaridavid.talkself.databinding.ItemChatRightBinding
import com.github.odaridavid.talkself.utils.VIEW_TYPE_MESSAGE_LEFT
import com.github.odaridavid.talkself.utils.VIEW_TYPE_MESSAGE_RIGHT
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.utils.UtilityFunctions


class ChatAdapter : ListAdapter<Chat, RecyclerView.ViewHolder>(
    ChatDiffUtil
) {

    var currentUser: User? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val rightBinding = ItemChatRightBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        if (viewType == VIEW_TYPE_MESSAGE_RIGHT) {

            return RightChatHolder(rightBinding)

        } else if (viewType == VIEW_TYPE_MESSAGE_LEFT) {

            val leftBinding = ItemChatLeftBinding.inflate(LayoutInflater.from(parent.context),parent,false)

            return LeftChatHolder(leftBinding)
        }

        return RightChatHolder(rightBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        
        val previousMessage = if (position >= 1) getItem(position - 1) else null

        when (holder.itemViewType) {
            
            VIEW_TYPE_MESSAGE_RIGHT -> (holder as RightChatHolder).bind(
                previousMessage,
                message
            )

            VIEW_TYPE_MESSAGE_LEFT -> (holder as LeftChatHolder).bind(
                previousMessage,
                message
            )
        }


    }

    private class LeftChatHolder(val binding: ItemChatLeftBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(previousChat: Chat?, chat: Chat) {

            binding.apply {

                val date = chat.timesent?.let { UtilityFunctions.formatMillisecondsToDate(it) }
                val prevDate = chat.timesent?.let { UtilityFunctions.formatMillisecondsToDate(it) }

                textGchatMessageOther.text = chat.message
                textGchatTimestampOther.text = chat.timesent?.let { UtilityFunctions.formatMillisecondsToTime(it) }
                textGchatUserOther.text = chat.username
                textGchatDateOther.text = date?.split(",")?.first()

                if (previousChat != null && previousChat.userid == chat.userid){
                    textGchatUserOther.visibility = View.GONE
                    imageGchatProfileOther.visibility = View.GONE

                }else{

                    textGchatUserOther.visibility = View.VISIBLE
                    imageGchatProfileOther.visibility = View.VISIBLE

                }

                when {
                    previousChat == null -> {
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

    private class RightChatHolder(val binding:  ItemChatRightBinding) : RecyclerView.ViewHolder(binding.root) {
        

        fun bind(previousChat: Chat?, chat: Chat) {

            binding.apply {

                val date = chat.timesent?.let { UtilityFunctions.formatMillisecondsToDate(it) }
                val prevDate = chat.timesent?.let { UtilityFunctions.formatMillisecondsToDate(it) }


                textGchatMessageRight.text = chat.message
                textGchatUserDate.text = date?.split(",")?.first()
                textGchatNameRight.text = chat.username
                textGchatTimestampMe.text = chat.timesent?.let { UtilityFunctions.formatMillisecondsToTime(it) }

                if (previousChat != null && previousChat.userid == chat.userid){
                    textGchatNameRight.visibility = View.GONE
                    imageGchatProfileOther.visibility = View.GONE
                }else{
                    textGchatNameRight.visibility = View.VISIBLE
                    imageGchatProfileOther.visibility = View.VISIBLE
                }
                when {
                    previousChat == null -> {
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
        val chat = getItem(position)
        return if (chat.userid == currentUser?.id) {
            // If the current user is the sender of the message
            VIEW_TYPE_MESSAGE_RIGHT
        } else {
            // If some other user sent the message
            VIEW_TYPE_MESSAGE_LEFT
        }
    }

    fun getItemAt(position: Int): Chat {
        return getItem(position)
    }

    companion object {
        val ChatDiffUtil = object : DiffUtil.ItemCallback<Chat>() {
            override fun areItemsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Chat, newItem: Chat): Boolean {
                return oldItem == newItem
            }
        }
    }
}