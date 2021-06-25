package com.github.odaridavid.talkself.ui.fragments.memoji

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.databinding.ItemMemojiBinding

class MemojiAdapter(private val call: (Int) -> Unit) : ListAdapter<Int, MemojiAdapter.ViewHolder>(
    ConversationDiffUtil
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemMemojiBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val drawable = getItem(position)

        Glide.with(holder.binding.imageViewMemoji)
            .load(drawable)
            .circleCrop()
            .placeholder(R.drawable.ic_koala)
            .error(R.drawable.ic_koala)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.binding.imageViewMemoji)

        holder.itemView.setOnClickListener {
            call(drawable)
        }
    }

    class ViewHolder(val binding: ItemMemojiBinding) : RecyclerView.ViewHolder(binding.root)

    companion object {
        val ConversationDiffUtil = object : DiffUtil.ItemCallback<Int>() {
            override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
               return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
                return oldItem == newItem
            }


        }
    }


}