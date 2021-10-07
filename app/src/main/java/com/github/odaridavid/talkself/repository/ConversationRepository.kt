package com.github.odaridavid.talkself.repository

import com.github.odaridavid.talkself.data.room.ConversationDao
import com.github.odaridavid.talkself.models.Conversation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

internal class ConversationRepository @Inject constructor(
    private val conversationDao: ConversationDao,
    private val dispatcher: CoroutineDispatcher
) {

    suspend fun addConversation(conversation: Conversation) {
        conversationDao.insertConversation(conversation)
    }

    suspend fun updateConversation(conversation: Conversation) {
        conversationDao.updateConversation(conversation)
    }

    fun deleteConversation(conversation: Conversation) {
        conversationDao.deleteConversation(conversation)
    }

    fun getAllConversations() = conversationDao.getAllConversations()

    // TODO Getting all conversations should be more than enough to invalidate this extra query
    val conversationsandusers = conversationDao.getAllConversationsAndUsers()
}
