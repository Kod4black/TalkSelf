package com.github.odaridavid.talkself.data

import com.github.odaridavid.talkself.data.local.conversation.ConversationDao
import com.github.odaridavid.talkself.data.local.conversation.toDomain
import com.github.odaridavid.talkself.domain.models.Conversation
import com.github.odaridavid.talkself.domain.models.toDbEntity
import com.github.odaridavid.talkself.domain.repository.ConversationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ConversationRepositoryImpl(
    private val conversationDao: ConversationDao,
) : ConversationRepository {

    override suspend fun addConversation(conversation: Conversation) {
        val conversationEntity = conversation.toDbEntity()
        conversationDao.insertConversation(conversationEntity)
    }

    override suspend fun updateConversation(conversation: Conversation) {
        val conversationEntity = conversation.toDbEntity()
        conversationDao.updateConversation(conversationEntity)
    }

    override fun deleteConversation(conversation: Conversation) {
        val conversationEntity = conversation.toDbEntity()
        conversationDao.deleteConversation(conversationEntity)
    }

    override fun getAllConversations(): Flow<List<Conversation>> =
        conversationDao.getAllConversations().map { conversationEntities ->
            conversationEntities.map { conversationEntity ->
                conversationEntity.toDomain()
            }
        }

    // TODO Getting all conversations should be more than enough to invalidate this extra query
   override val conversationsandusers = conversationDao.getAllConversationsAndUsers()
}
