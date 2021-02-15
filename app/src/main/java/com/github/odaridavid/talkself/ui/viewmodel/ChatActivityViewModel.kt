package com.github.odaridavid.talkself.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.repository.MainRepository
import kotlinx.coroutines.launch

class ChatActivityViewModel @ViewModelInject constructor(private val mainRepository: MainRepository) : ViewModel() {

    private var _chatList  = mainRepository.chats

    val chatList: LiveData<List<Chat>>
    get() = _chatList

    fun addText(chat: Chat){
        viewModelScope.launch {
            mainRepository.addChat(chat)
        }
    }

}