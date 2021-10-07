package com.github.odaridavid.talkself.ui.fragments.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(private val mainRepository: MainRepository) :
    ViewModel() {

    //Make an instance of ConversationsToolbarStateManager and make a getter variable for it
    private val _stateManager = ConversationsToolbarStateManager()
    val stateManager
        get() = _stateManager

    val conversation = mainRepository.conversations.asLiveData()
    val conversationAndUser = mainRepository.conversationsandusers.asLiveData()


    fun makeConversation(conversation: Conversation) {
        viewModelScope.launch {
            mainRepository.addConversation(conversation)
        }
    }

    fun deleteConversation(conversation: Conversation) {
        viewModelScope.launch {
            mainRepository.deleteConversation(conversation)
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            mainRepository.addUser(user)
        }
    }

}
