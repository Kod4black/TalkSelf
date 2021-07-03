package com.github.odaridavid.talkself.ui.fragments.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    fun users(conversationId : Int) = mainRepository.users(conversationId)
    fun chats(conversationId : Int) = mainRepository.chats(conversationId)


    //add add a chat to the chat table
    fun addText(chat: Chat){
        Coroutines.io {
            mainRepository.addChat(chat)
        }
    }

    //add a new user to user table
    fun addUser(user: User){
        Coroutines.io {
            mainRepository.addUser(user)
        }
    }

    //get user from room
    fun getUser(userId : Int) : User {
        return mainRepository.getUser(userId)
    }

    //update a conversation in the conversation table
    fun updateConversation(conversation: Conversation?) {
        Coroutines.io {
            mainRepository.updateConversation(conversation!!)
        }
    }
                                                                      
    //Create a new conversation in the conversation table
    fun makeConversation(conversation: Conversation){
        Coroutines.io {
            mainRepository.addConversation(conversation)
        }
    }

    //delete a conversation in conversation table
    fun deleteConversation(conversation: Conversation?){
        Coroutines.io {
            mainRepository.deleteConversation(conversation!!)
        }
    }

    //update a chat
    fun updatechat(chat : Chat){
        Coroutines.io{
            mainRepository.updateChat(chat)
        }
    }

}