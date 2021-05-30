package com.github.odaridavid.talkself.ui.fragments.user

import androidx.lifecycle.ViewModel
import com.github.odaridavid.talkself.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserFragmentViewModel  @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    fun users(conversationId : Int) = mainRepository.users(conversationId)


}