 package com.github.odaridavid.talkself.ui.fragments.chat;

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
import com.github.odaridavid.talkself.utils.ExtensionFunctions


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
        val previousmessage = if (position >= 1) getItem(position - 1) else null

        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageHolder).bind(
                previousmessage,
                message
            )

            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageHolder).bind(
                previousmessage,
                message
            )
        }


    }

    private class ReceivedMessageHolder internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        var messageText: TextView = itemView.findViewById<View>(R.id.text_gchat_message_other) as TextView
        var timeText: TextView = itemView.findViewById<View>(R.id.text_gchat_timestamp_other) as TextView
        var nameText: TextView = itemView.findViewById<View>(R.id.text_gchat_user_other) as TextView

        fun bind(previousmessage: Chat?, message: Chat) {
            messageText.text = message.message
            // Format the stored timestamp into a readable String using method.
            timeText.text = message.timesent?.let { ExtensionFunctions.formatMillisecondsToTime(it) }
            nameText.text = message.username
            if (previousmessage != null && previousmessage.userid == message.userid){
                nameText.visibility = View.GONE
            }else{
                nameText.visibility = View.VISIBLE
            }
        }

    }

    private class SentMessageHolder  constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var messageText: TextView = itemView.findViewById<View>(R.id.text_gchat_message_me) as TextView
        var timeText: TextView = itemView.findViewById(R.id.text_gchat_timestamp_me) as TextView
        var nameText: TextView = itemView.findViewById(R.id.text_gchat_user_me) as TextView

        fun bind(previousmessage: Chat?, message: Chat) {

            messageText.text = message.message
            nameText.text = message.username
            // Format the stored timestamp into a readable String using method.
            timeText.text = message.timesent?.let { ExtensionFunctions.formatMillisecondsToTime(it) }

            if (previousmessage != null && previousmessage.userid == message.userid){
                nameText.visibility = View.GONE
            }else{
                nameText.visibility = View.VISIBLE

            }
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

    fun getItemat(position: Int): Chat {
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