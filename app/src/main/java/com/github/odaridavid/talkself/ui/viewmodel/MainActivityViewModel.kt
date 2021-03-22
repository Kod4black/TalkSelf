package com.github.odaridavid.talkself.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.github.odaridavid.talkself.repository.MainRepository

class MainActivityViewModel @ViewModelInject constructor(private val mainRepository: MainRepository) : ViewModel() {
    val conversation = mainRepository.conversations

}