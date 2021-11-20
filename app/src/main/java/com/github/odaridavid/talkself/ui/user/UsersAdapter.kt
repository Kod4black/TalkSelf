package com.github.odaridavid.talkself.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.common.bindImage
import com.github.odaridavid.talkself.databinding.ItemUserBinding
import com.github.odaridavid.talkself.ui.models.UserUiModel

internal class UsersAdapter :
    ListAdapter<UserUiModel, UsersAdapter.UserViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemUserBinding =
            ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(itemUserBinding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)

        holder.itemView.setOnClickListener {
            // TODO Move Navigation Logic to view
            val action = UsersFragmentDirections.usersToEditUser(user)
            it.findNavController().navigate(action)
        }
    }

    class UserViewHolder(private val itemUserBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(itemUserBinding.root) {

        fun bind(userEntity: UserUiModel) {
            itemUserBinding.textViewName.text = userEntity.name
            itemUserBinding.shapeableImageView.apply {
                bindImage(imageUrl = userEntity.imageUri!!, context = context)
            }
        }
    }

    companion object {
        private val diffUtil = object : DiffUtil.ItemCallback<UserUiModel>() {
            override fun areItemsTheSame(oldItem: UserUiModel, newItem: UserUiModel): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: UserUiModel, newItem: UserUiModel): Boolean {
                return oldItem.userId == newItem.userId
            }
        }
    }
}
