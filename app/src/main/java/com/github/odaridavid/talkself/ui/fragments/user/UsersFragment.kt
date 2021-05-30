package com.github.odaridavid.talkself.ui.fragments.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.github.odaridavid.talkself.databinding.FragmentUsersBinding
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.utils.ExtensionFunctions.Companion.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : Fragment() {

    private val viewmodel by viewModels<UserFragmentViewModel>()
    private lateinit var binding: FragmentUsersBinding
    private var conversation: Conversation? = null
    private lateinit var userAdapter : UsersAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUsersBinding.inflate(inflater,container,false)
        conversation = arguments?.getParcelable("conversation")

        bindUI()
        return binding.root
    }

    private fun bindUI() {

        binding.apply {

            users.apply {
                userAdapter = UsersAdapter()
                adapter = userAdapter
                layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL)
            }

        }

        viewmodel.users(conversation?.id!!).observe(viewLifecycleOwner, {
                userAdapter.submitList(it)
        })
    }



}