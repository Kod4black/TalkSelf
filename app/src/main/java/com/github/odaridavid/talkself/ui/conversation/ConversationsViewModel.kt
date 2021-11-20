package com.github.odaridavid.talkself.ui.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.data.local.models.UserEntity
import com.github.odaridavid.talkself.data.repository.ConversationRepository
import com.github.odaridavid.talkself.data.repository.UserRepository
import com.github.odaridavid.talkself.ui.models.ConversationUiModel
import com.github.odaridavid.talkself.ui.models.UserUiModel
import com.github.odaridavid.talkself.ui.models.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ConversationsViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    //Make an instance of ConversationsToolbarStateManager and make a getter variable for it
    private val _stateManager = ConversationsToolbarStateManager()
    val stateManager
        get() = _stateManager

    val conversation = conversationRepository.getAllConversations().asLiveData()
    val conversationAndUser = conversationRepository.conversationsandusers.asLiveData()

    fun makeConversation(conversationUiModel: ConversationUiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val conversation = conversationUiModel.toDomain()
            conversationRepository.addConversation(conversation)
        }
    }

    fun deleteConversation(conversationUiModel: ConversationUiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val conversation = conversationUiModel.toDomain()
            conversationRepository.deleteConversation(conversation)
        }
    }

    // TODO Look into why a user is being added independent of a conversation
    fun addUser(userUiModel: UserUiModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userUiModel.toDomain()
            userRepository.addUser(user)
        }
    }

}
