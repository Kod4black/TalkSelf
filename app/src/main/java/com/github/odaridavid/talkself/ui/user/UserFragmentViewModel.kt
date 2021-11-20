package com.github.odaridavid.talkself.ui.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.github.odaridavid.talkself.data.UserRepository
import com.github.odaridavid.talkself.ui.models.UserUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
internal class UserFragmentViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    // TODO Is this Viewmodel needed or is this done elsewhere too? looks familiar
    fun getUsersInConversation(conversationId: Int) =
        userRepository.getUsersInConversation(conversationId).map{ users->
            users.map {  user ->
                UserUiModel(
                    conversationId = user.conversationId,
                    userId = user.userId,
                    color = user.color,
                    imageUri = user.imageUri,
                    name = user.name
                )
            }
        }.asLiveData()
}
