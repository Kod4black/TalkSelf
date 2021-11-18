package com.github.odaridavid.talkself.data.repository

import com.github.odaridavid.talkself.data.local.UserDao
import com.github.odaridavid.talkself.domain.User
import com.github.odaridavid.talkself.domain.toDbEntity
import com.github.odaridavid.talkself.ui.models.UserUiModel
import com.github.odaridavid.talkself.ui.models.toDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserRepository @Inject constructor(
    private val userDao: UserDao
) {

    fun getUsersInConversation(conversationId: Int): Flow<List<User>> =
        userDao.getUsers(conversationId).map { userEntities ->
            userEntities.map {
                User(
                    name = it.name,
                    conversationId = it.conversationId,
                    imageUri = it.imageUri,
                    color = it.color,
                    userId = it.userId
                )
            }
        }

    fun addUser(user: User) {
        val userEntity = user.toDbEntity()
        userDao.addUser(userEntity)
    }

    suspend fun updateUser(userUiModel: UserUiModel) {
        val userEntity = userUiModel.toDbEntity()
        userDao.updateUser(userEntity)
    }
}

