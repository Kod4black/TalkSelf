package com.github.odaridavid.talkself.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.repository.MainRepository
import com.github.odaridavid.talkself.utils.Coroutines
import com.google.android.material.shape.CornerSize
import kotlinx.coroutines.launch

class ChatActivityViewModel @ViewModelInject constructor(private val mainRepository: MainRepository) : ViewModel() {

    var chatList  = mainRepository.chats
    fun users(conversationid : Int) = mainRepository.users(conversationid)
    fun chats(conversationid : Int) = mainRepository.chats(conversationid)

    fun addText(chat: Chat){
        Coroutines.io {
            mainRepository.addChat(chat)
        }
    }

    fun addUser(user: User){
        Coroutines.io {
            mainRepository.addUser(user)
        }
    }

    fun updateConversation(conversation: Conversation?) {
        Coroutines.io {
            mainRepository.addconversation(conversation!!)
        }
    }

}