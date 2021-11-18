package com.github.odaridavid.talkself.ui.edituser

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.common.UtilityFunctions.Companion.bindImage
import com.github.odaridavid.talkself.databinding.ItemMemojiBinding

class MemojiAdapter(private val call: (String) -> Unit) : ListAdapter<String, MemojiAdapter.ViewHolder>(
    ConversationDiffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemMemojiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imageUrl = getItem(position)

        holder.binding.imageViewMemoji.apply {
            context.bindImage(imageUrl,this)
        }


        holder.itemView.setOnClickListener {
            call(imageUrl)
        }
    }

    class ViewHolder(val binding: ItemMemojiBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val ConversationDiffUtil = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
               return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }


        }
    }


}
