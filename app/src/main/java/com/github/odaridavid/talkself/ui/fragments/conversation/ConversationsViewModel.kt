package com.github.odaridavid.talkself.ui.fragments.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.repository.ConversationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ConversationsViewModel @Inject constructor(
    private val conversationRepository: ConversationRepository
) : ViewModel() {

    //Make an instance of ConversationsToolbarStateManager and make a getter variable for it
    private val _stateManager = ConversationsToolbarStateManager()
    val stateManager
        get() = _stateManager

    val conversation = conversationRepository.getAllConversations().asLiveData()
    val conversationAndUser = conversationRepository.conversationsandusers.asLiveData()


    fun makeConversation(conversation: Conversation) {
        viewModelScope.launch {
            conversationRepository.addConversation(conversation)
        }
    }

    fun deleteConversation(conversation: Conversation) {
        viewModelScope.launch {
            conversationRepository.deleteConversation(conversation)
        }
    }

    // TODO Look into why a user is being added independent of a conversation
    fun addUser(user: User) {
        viewModelScope.launch {
//            conversationRepository.addUser(user)
        }
    }

}
