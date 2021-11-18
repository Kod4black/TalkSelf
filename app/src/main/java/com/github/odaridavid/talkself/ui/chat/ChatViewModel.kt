package com.github.odaridavid.talkself.ui.chat

import androidx.lifecycle.*
import com.github.odaridavid.talkself.data.local.models.ChatEntity
import com.github.odaridavid.talkself.data.repository.MessagesRepository
import com.github.odaridavid.talkself.data.repository.ConversationRepository
import com.github.odaridavid.talkself.data.repository.UserRepository
import com.github.odaridavid.talkself.ui.models.ConversationUiModel
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

    // FIXME Mutable data source being exposed to the view from ViewModel
    var currentuser = MutableLiveData<UserUiModel>()

    fun getUsersInConversation(conversationId: Int): LiveData<List<UserUiModel>> =
        userRepository.getUsersInConversation(conversationId).map { users->
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


    // FIXME Use different chat model for ui and data layer and create mappers
    fun addMessage(chatEntity: ChatEntity) {
        viewModelScope.launch {
            messagesRepository.addMessage(chatEntity)
        }
    }

    // TODO Why not just update once a new message is added by observing the table/ makes sense for
    //  editing existing messages
    fun updatechat(chatEntity: ChatEntity) {
        viewModelScope.launch {
            messagesRepository.updateMessage(chatEntity)
        }
    }

    // TODO Look into why do we need to update a conversation when a message is added,if its for preview purposes a different approach could be used
    fun updateConversation(conversationUiModel: ConversationUiModel) {
        viewModelScope.launch {
            val conversation = conversationUiModel.toDomain()
            conversationRepository.updateConversation(conversation)
        }
    }

}
