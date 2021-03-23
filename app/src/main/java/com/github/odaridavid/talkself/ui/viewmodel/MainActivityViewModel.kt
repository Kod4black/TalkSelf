package com.github.odaridavid.talkself.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.repository.MainRepository
import com.github.odaridavid.talkself.utils.Coroutines

class MainActivityViewModel @ViewModelInject constructor(private val mainRepository: MainRepository) : ViewModel() {
    val conversation = mainRepository.conversations

     fun makeconversation(conversation: Conversation){
         Coroutines.io {
             mainRepository.addconversation(conversation)
         }
    }

    fun deleteConversation(conversation: Conversation){
        Coroutines.io {
            mainRepository.deleteConversation(conversation)
        }
    }

}