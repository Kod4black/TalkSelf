package com.github.odaridavid.talkself.ui.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.data.ConversationRepository
import com.github.odaridavid.talkself.data.UserRepository
import com.github.odaridavid.talkself.domain.toUiModel
import com.github.odaridavid.talkself.ui.models.ConversationUiModel
import com.github.odaridavid.talkself.ui.models.UserUiModel
import com.github.odaridavid.talkself.ui.models.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ConversationsViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _stateManager = ConversationsToolbarStateManager()
    val stateManager
        get() = _stateManager

    val conversation = conversationRepository.getAllConversations().map { conversation ->
        conversation.map { it.toUiModel() }
    }.asLiveData()

    //TODO Don't use db entities
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
