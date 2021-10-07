package com.github.odaridavid.talkself.ui.fragments.edituser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.repository.UserRepository
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.notifyObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var user = MutableLiveData<User>()

    var name = MutableLiveData<String>()
    var image = MutableLiveData<String>()
    var color = MutableLiveData<String>()

    fun updateName(string: String) {
        name.value = string
        name.notifyObserver()
    }

    fun updateImage(string: String) {
        image.value = string
        image.notifyObserver()
    }

    fun updateColor(string: String) {
        color.value = string
        color.notifyObserver()
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
        }
    }

}
