package com.github.odaridavid.talkself.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.repository.MainRepository
import com.github.odaridavid.talkself.utils.Coroutines
import kotlinx.coroutines.launch

class MainActivityViewModel @ViewModelInject constructor(private val mainRepository: MainRepository) : ViewModel() {
    val conversation = mainRepository.conversations

     fun makeconversation(conversation: Conversation){
         Coroutines.io {
             mainRepository.addconversation(conversation)
         }
    }

    fun deleteConversation(conversation: Conversation){
        viewModelScope.launch {
            mainRepository.deleteConversation(conversation)
        }
    }

    fun updateConversation(conversation: Conversation){
        viewModelScope.launch {
            mainRepository.updateconversation(conversation)
        }
    }


    fun deleteChats(conversation: Conversation){
        mainRepository.deleteChats(conversation)
    }



}