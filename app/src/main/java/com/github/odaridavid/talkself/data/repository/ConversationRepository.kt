package com.github.odaridavid.talkself.data.repository

import com.github.odaridavid.talkself.data.local.ConversationDao
import com.github.odaridavid.talkself.data.local.models.ConversationEntity
import com.github.odaridavid.talkself.domain.Conversation
import com.github.odaridavid.talkself.domain.toDbEntity
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class ConversationRepository @Inject constructor(
    private val conversationDao: ConversationDao
) {

    suspend fun addConversation(conversation: Conversation) {
        val conversationEntity = conversation.toDbEntity()
        conversationDao.insertConversation(conversationEntity)
    }

    suspend fun updateConversation(conversation: Conversation) {
        val conversationEntity = conversation.toDbEntity()
        conversationDao.updateConversation(conversationEntity)
    }

    fun deleteConversation(conversation: Conversation) {
        val conversationEntity = conversation.toDbEntity()
        conversationDao.deleteConversation(conversationEntity)
    }

    fun getAllConversations() = conversationDao.getAllConversations()

    // TODO Getting all conversations should be more than enough to invalidate this extra query
    val conversationsandusers = conversationDao.getAllConversationsAndUsers()
}
