package com.github.odaridavid.talkself.ui.fragments.edituser

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.repository.MainRepository
import com.github.odaridavid.talkself.utils.Coroutines
import com.github.odaridavid.talkself.utils.UtilityFunctions.Companion.notifyObserver
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EditUserViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    var user = MutableLiveData<User>()

    var name = MutableLiveData<String>()
    var image = MutableLiveData<String>()
    var color = MutableLiveData<String>()

    fun updateName(string: String){
        name.value = string
        name.notifyObserver()
    }

    fun updateImage(string: String){
        image.value = string
        image.notifyObserver()
    }

    fun updateColor(string: String){
        color.value = string
        color.notifyObserver()
    }

    fun updateUser(user: User){
        Coroutines.io {
            mainRepository.updateUser(user)
        }
    }

}