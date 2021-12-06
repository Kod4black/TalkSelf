package com.github.odaridavid.talkself.data

import com.github.odaridavid.talkself.data.local.messages.MessagesDao
import com.github.odaridavid.talkself.data.local.messages.MessageEntity
import com.github.odaridavid.talkself.domain.Message
import com.github.odaridavid.talkself.domain.toDbEntity
import javax.inject.Inject

internal class MessagesRepository @Inject constructor(
    private val messagesDao: MessagesDao
) {

    fun updateMessage(message: Message) {
        val messageEntity = message.toDbEntity()
        messagesDao.updateMessage(messageEntity)
    }

    suspend fun addMessage(message: Message) {
        val messageEntity = message.toDbEntity()
        messagesDao.addMessage(messageEntity)
    }

    fun getMessagesInConversation(conversationId: Int) =
        messagesDao.getConversationMessages(conversationId)
}
