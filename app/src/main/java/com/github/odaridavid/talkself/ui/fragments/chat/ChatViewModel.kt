package com.github.odaridavid.talkself.ui.fragments.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    var currentuser = MutableLiveData<User>()

    fun users(conversationId: Int) = mainRepository.users(conversationId).asLiveData()
    fun chats(conversationId: Int) = mainRepository.chats(conversationId).asLiveData()


    fun addText(chat: Chat) {
        viewModelScope.launch {
            mainRepository.addChat(chat)
        }
    }

    fun updateConversation(conversation: Conversation?) {
        viewModelScope.launch {
            mainRepository.updateConversation(conversation!!)
        }
    }

    fun updatechat(chat: Chat) {
        viewModelScope.launch {
            mainRepository.updateChat(chat)
        }
    }

}
