package com.github.odaridavid.talkself.ui.fragments.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.databinding.FragmentUsersBinding
import com.github.odaridavid.talkself.databinding.ItemUserBinding
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.utils.interfaces.ImageClick

class UsersAdapter(
    private val fragmentUsersBinding: FragmentUsersBinding,
    private val callback: OnBackPressedCallback,
    private val imageClick: ImageClick,
) : ListAdapter<User, UsersAdapter.ViewHolder>(UserDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemUserBinding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemUserBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {

            imageClick.onImageClick(holder.itemUserBinding.transformationLayout, user)

            holder.itemUserBinding.transformationLayout.apply {

            }

        }
    }

    class ViewHolder(val itemUserBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(itemUserBinding.root) {

        fun bind(user: User) {
            itemUserBinding.textViewName.text = user.name

            Glide.with(itemUserBinding.shapeableImageView)
                .load(user.imageUri)
                .placeholder(R.drawable.ic_koala)
                .error(R.drawable.ic_koala)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(itemUserBinding.shapeableImageView)
        }

    }



    companion object {

        val UserDiffUtil = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.id == newItem.id
            }

        }

    }

}