package com.github.odaridavid.talkself.data

import com.github.odaridavid.talkself.data.local.conversation.ConversationDao
import com.github.odaridavid.talkself.data.local.conversation.toDomain
import com.github.odaridavid.talkself.domain.Conversation
import com.github.odaridavid.talkself.domain.toDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    fun getAllConversations(): Flow<List<Conversation>> =
        conversationDao.getAllConversations().map { conversationEntities ->
            conversationEntities.map { conversationEntity ->
                conversationEntity.toDomain()
            }
        }

    // TODO Getting all conversations should be more than enough to invalidate this extra query
    val conversationsandusers = conversationDao.getAllConversationsAndUsers()
}
