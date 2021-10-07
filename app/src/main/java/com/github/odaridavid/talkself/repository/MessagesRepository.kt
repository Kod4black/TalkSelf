package com.github.odaridavid.talkself.repository

import com.github.odaridavid.talkself.data.room.ChatDao
import com.github.odaridavid.talkself.models.Chat
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class MessagesRepository @Inject constructor(
    private val chatDao: ChatDao,
    private val dispatcher: CoroutineDispatcher
) {

    fun updateMessage(chat: Chat) {
        chatDao.updateChat(chat)
    }

    suspend fun addMessage(chat: Chat) {
        chatDao.addChat(chat)
    }

    fun getMessagesInConversation(conversationId: Int) = chatDao.getChats(conversationId)

}
