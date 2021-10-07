package com.github.odaridavid.talkself.repository

import com.github.odaridavid.talkself.data.room.UserDao
import com.github.odaridavid.talkself.models.User
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

internal class UserRepository @Inject constructor(
    private val userDao: UserDao,
    private val dispatcher: CoroutineDispatcher
) {

    fun getUsersInConversation(conversationId: Int) = userDao.getUsers(conversationId)

    fun addUser(user: User, conversationId: Int? = null) {
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }
}

