package com.github.odaridavid.talkself.ui.fragments.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.github.odaridavid.talkself.R
import com.github.odaridavid.talkself.databinding.ItemUserBinding
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.bindImage
import com.github.odaridavid.talkself.utils.interfaces.ImageClick

class UsersAdapter(
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
            val action = UsersFragmentDirections.usersToEditUser(user)
            it.findNavController().navigate(action)
        }
    }

    class ViewHolder(val itemUserBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(itemUserBinding.root) {

        fun bind(user: User) {
            itemUserBinding.textViewName.text = user.name
            itemUserBinding.shapeableImageView.apply {
                context.bindImage(user.imageUri!!, this)
            }
        }

    }



    companion object {

        val UserDiffUtil = object : DiffUtil.ItemCallback<User>() {

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.userId == newItem.userId
            }

        }

    }

}