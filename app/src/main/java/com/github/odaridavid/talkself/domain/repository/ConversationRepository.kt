package com.github.odaridavid.talkself.domain.repository

import com.github.odaridavid.talkself.data.local.relations.ConversationAndUser
import com.github.odaridavid.talkself.domain.models.Conversation
import kotlinx.coroutines.flow.Flow

interface ConversationRepository {
    suspend fun addConversation(conversation: Conversation)
    suspend fun updateConversation(conversation: Conversation)
    fun deleteConversation(conversation: Conversation)
    fun getAllConversations(): Flow<List<Conversation>>
    val conversationsandusers: Flow<List<ConversationAndUser>>
}