package com.github.odaridavid.talkself.ui.edituser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.common.bindImage
import com.github.odaridavid.talkself.databinding.ItemMemojiBinding

internal class MemojiAdapter(
    private val call: (String) -> Unit
) : ListAdapter<String, MemojiViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemojiViewHolder {
        val binding = ItemMemojiBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MemojiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MemojiViewHolder, position: Int) {
        val imageUrl = getItem(position)

        holder.binding.imageViewMemoji.apply {
            bindImage(imageUrl = imageUrl, context = context)
        }

        holder.itemView.setOnClickListener {
            call(imageUrl)
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class MemojiViewHolder(val binding: ItemMemojiBinding) :
    RecyclerView.ViewHolder(binding.root)
