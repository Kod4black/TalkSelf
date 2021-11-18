package com.github.odaridavid.talkself.ui.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.odaridavid.talkself.common.UserProfileInteractions
import com.github.odaridavid.talkself.common.UtilityFunctions.Companion.bindImage
import com.github.odaridavid.talkself.databinding.ItemUserBinding
import com.github.odaridavid.talkself.ui.models.UserUiModel

class UsersAdapter(
    private val userProfileInteractions: UserProfileInteractions,
) : ListAdapter<UserUiModel, UsersAdapter.ViewHolder>(diffUtil) {

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

    class ViewHolder(private val itemUserBinding: ItemUserBinding) :
        RecyclerView.ViewHolder(itemUserBinding.root) {

        fun bind(userEntity: UserUiModel) {
            itemUserBinding.textViewName.text = userEntity.name
            itemUserBinding.shapeableImageView.apply {
                context.bindImage(userEntity.imageUri!!, this)
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
