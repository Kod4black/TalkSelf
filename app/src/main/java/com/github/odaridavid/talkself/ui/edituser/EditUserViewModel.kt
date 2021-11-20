package com.github.odaridavid.talkself.ui.edituser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.data.repository.UserRepository
import com.github.odaridavid.talkself.ui.models.UserUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class EditUserViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private var _user = MutableLiveData<UserUiModel>()
    val user: LiveData<UserUiModel>
        get() = _user

    private var _name = MutableLiveData<String>()
    val name: LiveData<String>
        get() = _name

    private var _imageUrl = MutableLiveData<String>()
    val imageUrl: LiveData<String>
        get() = _imageUrl

    private var _color = MutableLiveData<String>()
    val color: LiveData<String>
        get() = _color

    fun updateName(string: String) {
        _name.value = string
    }

    fun updateImage(string: String) {
        _imageUrl.value = string
    }

    fun updateColor(string: String) {
        _color.value = string
    }

    fun updateUser(userUiModel: UserUiModel) {
        viewModelScope.launch {
            userRepository.updateUser(userUiModel)
        }
    }
}
