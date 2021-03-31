 package com.github.odaridavid.talkself.ui.adapter;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.utils.VIEW_TYPE_MESSAGE_RECEIVED
import com.github.odaridavid.talkself.utils.VIEW_TYPE_MESSAGE_SENT
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.utils.Utils


class ChatAdapter() : ListAdapter<Chat, RecyclerView.ViewHolder>(
    ChatDiffUtil
) {

    var currentUser: User? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var view = View(parent.context)
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_right, parent, false)
            return SentMessageHolder(view)
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
             view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat_left, parent, false)
            return ReceivedMessageHolder(view)
        }

        return SentMessageHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)

        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(
                message
            )
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(
                message
            )
        }
    }

    private class ReceivedMessageHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageText: TextView
        var timeText: TextView
        var nameText: TextView
        fun bind(message: Chat) {
            messageText.text = message.message
            // Format the stored timestamp into a readable String using method.
            timeText.text = message.timesent?.let { Utils.formatMillisecondsToTime(it) }
            nameText.text = message.username

        }

        init {
            messageText = itemView.findViewById<View>(R.id.text_gchat_message_other) as TextView
            timeText = itemView.findViewById<View>(R.id.text_gchat_timestamp_other) as TextView
            nameText = itemView.findViewById<View>(R.id.text_gchat_user_other) as TextView
        }
    }

    private class SentMessageHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageText: TextView = itemView.findViewById<View>(R.id.text_gchat_message_me) as TextView
        var timeText: TextView = itemView.findViewById(R.id.text_gchat_timestamp_me) as TextView
        var nameText: TextView = itemView.findViewById(R.id.text_gchat_user_me) as TextView

        fun bind(message: Chat) {
            messageText.text = message.message
            nameText.text = message.username

            // Format the stored timestamp into a readable String using method.
            timeText.text = message.timesent?.let { Utils.formatMillisecondsToTime(it) };
        }

    }


    override fun getItemViewType(position: Int): Int {
        val chat = getItem(position)
        return if (chat.userid == currentUser?.id) {
            // If the current user is the sender of the message
            VIEW_TYPE_MESSAGE_SENT
        } else {
            // If some other user sent the message
            VIEW_TYPE_MESSAGE_RECEIVED
        }
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