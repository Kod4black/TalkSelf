package com.github.odaridavid.talkself.ui.fragments.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.repository.MainRepository
import com.github.odaridavid.talkself.utils.Coroutines
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

    val conversation = mainRepository.conversations

    //    make an new conversation in the  conversation table
    fun makeConversation(conversation: Conversation) {
        Coroutines.io {
            mainRepository.addconversation(conversation)
        }
    }

    //    delete a conversation from conversation table
    fun deleteConversation(conversation: Conversation) {
        viewModelScope.launch {
            mainRepository.deleteConversation(conversation)
        }
    }

    //    update a conversation in the conversation table
    fun updateConversation(conversation: Conversation) {
        viewModelScope.launch {
            mainRepository.updateconversation(conversation)
        }
    }


    //    delete chats from a particular conversation
    fun deleteChats(conversation: Conversation) {
        mainRepository.deleteChats(conversation)
    }

    //    add a new user to user table
    fun addUser(user: User) {
        Coroutines.io {
            mainRepository.addUser(user)
        }
    }

}