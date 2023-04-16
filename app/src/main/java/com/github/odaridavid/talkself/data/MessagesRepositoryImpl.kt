package com.github.odaridavid.talkself.data

import com.github.odaridavid.talkself.data.local.messages.MessagesDao
import com.github.odaridavid.talkself.domain.models.Message
import com.github.odaridavid.talkself.domain.models.toDbEntity
import com.github.odaridavid.talkself.domain.repository.MessagesRepository

internal class MessagesRepositoryImpl(
    private val messagesDao: MessagesDao
) : MessagesRepository{

    override suspend fun updateMessage(message: Message) {
        val messageEntity = message.toDbEntity()
        messagesDao.updateMessage(messageEntity)
    }

    override suspend fun addMessage(message: Message) {
        val messageEntity = message.toDbEntity()
        messagesDao.addMessage(messageEntity)
    }

    override fun getMessagesInConversation(conversationId: Int) =
        messagesDao.getConversationMessages(conversationId)
}
