package com.github.odaridavid.talkself.ui.chat

import androidx.lifecycle.*
import com.github.odaridavid.talkself.data.ConversationRepository
import com.github.odaridavid.talkself.data.MessagesRepository
import com.github.odaridavid.talkself.data.UserRepository
import com.github.odaridavid.talkself.data.local.messages.MessageEntity
import com.github.odaridavid.talkself.ui.models.ConversationUiModel
import com.github.odaridavid.talkself.ui.models.MessageUiModel
import com.github.odaridavid.talkself.ui.models.UserUiModel
import com.github.odaridavid.talkself.ui.models.toDomain
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO Use Usecases instead of repositories when clean up is kind of done
@HiltViewModel
internal class ChatViewModel @Inject constructor(
    private val messagesRepository: MessagesRepository,
    private val conversationRepository: ConversationRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _currentUser = MutableLiveData<UserUiModel>()
    val currentUser: LiveData<UserUiModel>
        get() = _currentUser

    fun getUsersInConversation(conversationId: Int): LiveData<List<UserUiModel>> =
        userRepository.getUsersInConversation(conversationId).map { users ->
            users.map {
                UserUiModel(
                    color = it.color,
                    name = it.name,
                    userId = it.userId,
                    conversationId = it.conversationId,
                    imageUri = it.imageUri
                )
            }
        }.asLiveData()

    fun getMessagesInConversation(conversationId: Int) =
        messagesRepository.getMessagesInConversation(conversationId).asLiveData()

    fun addMessage(messageUiModel: MessageUiModel) {
        viewModelScope.launch {
            val message = messageUiModel.toDomain()
            messagesRepository.addMessage(message)
        }
    }

    // TODO Why not just update once a new message is added by observing the table/ makes sense for
    //  editing existing messages
    fun updateMessage(messageUiModel: MessageUiModel) {
        viewModelScope.launch {
            val message = messageUiModel.toDomain()
            messagesRepository.updateMessage(message)
        }
    }

    // TODO Look into why do we need to update a conversation when a message is added,if its for preview purposes a different approach could be used
    fun updateConversation(conversationUiModel: ConversationUiModel) {
        viewModelScope.launch {
            val conversation = conversationUiModel.toDomain()
            conversationRepository.updateConversation(conversation)
        }
    }

    fun updateUserUiModel(userUiModel: UserUiModel) {
        _currentUser.value = userUiModel
    }
}
