package com.github.odaridavid.talkself;

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_chat.view.*

/**
 *
 * Copyright 2020 David Odari
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *          http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 **/
class ChatAdapter(val currentUser: User) :
    ListAdapter<Chat, ChatAdapter.ChatViewHolder>(ChatDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int): Unit =
        getItem(position).let { holder.bind(it) }

    inner class ChatViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        //TODO Username adapt to layout nicely for small text
        fun bind(item: Chat) {
            view.chat_text_view.text = item.text
            if (item.userId == currentUser.id) {
                with(view.context) {
                    view.chat_layout.background = getDrawable(R.color.colorAccent)
                    view.username_text_view.text = getString(R.string.you)
                    view.username_text_view.setTextColor(
                        ContextCompat.getColor(
                            this,
                            android.R.color.black
                        )
                    )
                }
            } else {
                view.username_text_view.text = item.username
            }
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