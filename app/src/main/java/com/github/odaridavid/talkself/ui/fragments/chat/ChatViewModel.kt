package com.github.odaridavid.talkself.ui.fragments.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.repository.MessagesRepository
import com.github.odaridavid.talkself.repository.ConversationRepository
import com.github.odaridavid.talkself.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    var currentuser = MutableLiveData<User>()

    fun getUsersInConversation(conversationId: Int) =
        userRepository.getUsersInConversation(conversationId).asLiveData()

    fun getMessagesInConversation(conversationId: Int) =
        messagesRepository.getMessagesInConversation(conversationId).asLiveData()


    // FIXME Use different chat model for ui and data layer and create mappers
    fun addMessage(chat: Chat) {
        viewModelScope.launch {
            messagesRepository.addMessage(chat)
        }
    }

    // TODO Why not just update once a new message is added by observing the table/ makes sense for
    //  editing existing messages
    fun updatechat(chat: Chat) {
        viewModelScope.launch {
            messagesRepository.updateMessage(chat)
        }
    }

    // TODO Look into why do we need to update a conversation when a message is added,if its for preview purposes a different approach could be used
    fun updateConversation(conversation: Conversation) {
        viewModelScope.launch {
            conversationRepository.updateConversation(conversation)
        }
    }

}
