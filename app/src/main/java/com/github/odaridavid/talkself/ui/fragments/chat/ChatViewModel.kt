package com.github.odaridavid.talkself.ui.fragments.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.repository.MainRepository
import com.github.odaridavid.talkself.utils.Coroutines
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    var currentuser = MutableLiveData<User>()

    fun users(conversationId : Int) = mainRepository.users(conversationId).asLiveData()
    fun chats(conversationId : Int) = mainRepository.chats(conversationId).asLiveData()


    //add add a chat to the chat table
    fun addText(chat: Chat){
        Coroutines.io {
            mainRepository.addChat(chat)
        }
    }

    //update a conversation in the conversation table
    fun updateConversation(conversation: Conversation?) {
        Coroutines.io {
            mainRepository.updateConversation(conversation!!)
        }
    }

    //update a chat
    fun updatechat(chat : Chat){
        Coroutines.io{
            mainRepository.updateChat(chat)
        }
    }

}
