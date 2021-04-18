package com.github.odaridavid.talkself.ui.fragments.chat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.repository.MainRepository
import com.github.odaridavid.talkself.utils.Coroutines
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    var chatList  = mainRepository.chats
    var currentuser = MutableLiveData<User>()

    fun users(conversationid : Int) = mainRepository.users(conversationid)
    fun chats(conversationid : Int) = mainRepository.chats(conversationid)


//    add add a chat to the chat table
    fun addText(chat: Chat){
        Coroutines.io {
            mainRepository.addChat(chat)
        }
    }

//    add a new user to user table
    fun addUser(user: User){
        Coroutines.io {
            mainRepository.addUser(user)
        }
    }
//    update a conversation in the convesation table
    fun updateConversation(conversation: Conversation?) {
        Coroutines.io {
            mainRepository.updateconversation(conversation!!)
        }
    }
                                                                      
//    Create a new conversation in the conversation table
    fun makeconversation(conversation: Conversation){
        Coroutines.io {
            mainRepository.addconversation(conversation)
        }
    }

//    delete a conversation in conversation table
    fun deleteConversation(conversation: Conversation?){
        Coroutines.io {
            mainRepository.deleteConversation(conversation!!)
        }
    }

//    update a chat
    fun updatechat(chat : Chat){
        Coroutines.io{
            mainRepository.updateChat(chat)
        }
    }

}