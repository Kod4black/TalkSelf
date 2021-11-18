package com.github.odaridavid.talkself.ui.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.odaridavid.talkself.databinding.FragmentUsersBinding
import com.github.odaridavid.talkself.data.local.models.UserEntity
import com.github.odaridavid.talkself.common.UserProfileInteractions
import com.github.odaridavid.talkself.ui.models.ConversationUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : Fragment(), UserProfileInteractions {


    private val viewmodel by viewModels<UserFragmentViewModel>()
    private lateinit var binding: FragmentUsersBinding
    private var conversationUiModel: ConversationUiModel? = null
    private lateinit var userAdapter: UsersAdapter
    private val args: UsersFragmentArgs by navArgs()
    private lateinit var userEntity: UserEntity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater, container, false)

        conversationUiModel = args.conversation

        bindUI()

        return binding.root
    }

    private fun bindUI() {

        binding.apply {

            users.apply {
                userAdapter = UsersAdapter(this@UsersFragment)
                adapter = userAdapter
                layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
            }

            imageViewback.setOnClickListener {
                it.findNavController().navigateUp()
            }

        }

        viewmodel.getUsersInConversation(conversationUiModel?.conversationId!!).observe(viewLifecycleOwner, {
            userAdapter.submitList(it)
        })

        setFragmentResultListener("requestKey") { _, bundle ->
            val result = bundle.getInt("bundleKey")
            userEntity.imageUri = result.toString()
        }

    }

    override fun onImageClick(userEntity: UserEntity) {
        // to remove
    }
}
