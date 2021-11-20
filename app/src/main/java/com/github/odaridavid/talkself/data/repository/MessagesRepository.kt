package com.github.odaridavid.talkself.data.repository

import com.github.odaridavid.talkself.data.local.ChatDao
import com.github.odaridavid.talkself.data.local.models.ChatEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class MessagesRepository @Inject constructor(
    private val chatDao: ChatDao
) {

    fun updateMessage(chatEntity: ChatEntity) {
        chatDao.updateChat(chatEntity)
    }

    suspend fun addMessage(chatEntity: ChatEntity) {
        chatDao.addChat(chatEntity)
    }

    fun getMessagesInConversation(conversationId: Int) = chatDao.getChats(conversationId)

}
