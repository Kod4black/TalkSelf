 package com.github.odaridavid.talkself.ui.fragments.conversation;

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.utils.ExtensionFunctions
 class ConversationAdapter() : ListAdapter<Conversation, ConversationAdapter.ViewHolder>(
    ConversationDiffUtil
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.convesations, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val conversation = getItem(position)
        holder.bind(conversation)

//        holder.itemView.setOnClickListener {
//            Intent(holder.itemView.context, ChatActivity::class.java).also {
//                it.putExtra("conversation", conversation)
//                holder.itemView.context.startActivity(it)
//            }
//        }

        holder.cardview.setOnLongClickListener {
            return@setOnLongClickListener true
        }

    }

     class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        var lastman: TextView = itemView.findViewById<View>(R.id.conversation_last_man) as TextView
        var timeText: TextView = itemView.findViewById<View>(R.id.conversation_time) as TextView
        var messageText: TextView = itemView.findViewById<View>(R.id.text_chat_last_message) as TextView
         var cardview : CardView = itemView.findViewById(R.id.cardview)


        fun bind(message: Conversation) {
            messageText.text = message.lastMessage
            lastman.text = message.lastUser
            timeText.text = ExtensionFunctions.formatMillisecondsToDate(message.timeCreated!!)

        }

    }

     fun getItemat(position: Int): Conversation {
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



}