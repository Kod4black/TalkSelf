package com.github.odaridavid.talkself.domain.repository

import com.github.odaridavid.talkself.domain.models.User
import com.github.odaridavid.talkself.ui.models.UserUiModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsersInConversation(conversationId: Int): Flow<List<User>>
    fun addUser(user: User)
    suspend fun updateUser(userUiModel: UserUiModel)
}