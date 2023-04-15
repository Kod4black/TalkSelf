package com.github.odaridavid.talkself.domain.repository

import com.github.odaridavid.talkself.data.local.relations.MessageAndUser
import com.github.odaridavid.talkself.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun updateMessage(message: Message)
    suspend fun addMessage(message: Message)
    fun getMessagesInConversation(conversationId: Int): Flow<List<MessageAndUser>>
}