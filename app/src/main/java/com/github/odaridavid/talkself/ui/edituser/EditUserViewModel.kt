package com.github.odaridavid.talkself.ui.edituser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.data.local.models.UserEntity
import com.github.odaridavid.talkself.data.repository.UserRepository
import com.github.odaridavid.talkself.common.UtilityFunctions.Companion.notifyObserver
import com.github.odaridavid.talkself.ui.models.UserUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    var user = MutableLiveData<UserEntity>()

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

    fun updateUser(userUiModel: UserUiModel) {
        viewModelScope.launch {
            userRepository.updateUser(userUiModel)
        }
    }

}
