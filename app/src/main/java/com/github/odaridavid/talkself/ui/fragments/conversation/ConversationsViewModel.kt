package com.github.odaridavid.talkself.ui.fragments.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.repository.MainRepository
import com.github.odaridavid.talkself.utils.Coroutines
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationsViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {
    val conversation = mainRepository.conversations

//    make an new conversation in the  conversation table
     fun makeconversation(conversation: Conversation){
         Coroutines.io {
             mainRepository.addconversation(conversation)
         }
    }

//    delete a conversation from conversation table
    fun deleteConversation(conversation: Conversation){
        viewModelScope.launch {
            mainRepository.deleteConversation(conversation)
        }
    }

//    update a conversation in the conversation table
    fun updateConversation(conversation: Conversation){
        viewModelScope.launch {
            mainRepository.updateconversation(conversation)
        }
    }


//    delete chats from a particular conversation
    fun deleteChats(conversation: Conversation){
        mainRepository.deleteChats(conversation)
    }



}