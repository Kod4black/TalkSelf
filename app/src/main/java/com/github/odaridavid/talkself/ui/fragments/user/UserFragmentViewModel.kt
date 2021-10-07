package com.github.odaridavid.talkself.ui.fragments.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.github.odaridavid.talkself.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class UserFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // TODO Is this Viewmodel needed or is this done elsewhere too? looks familiar
    fun getUsersInConversation(conversationId: Int) =
        userRepository.getUsersInConversation(conversationId).asLiveData()
}
