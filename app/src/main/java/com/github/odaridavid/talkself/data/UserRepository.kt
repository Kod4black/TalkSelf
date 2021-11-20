package com.github.odaridavid.talkself.data

import com.github.odaridavid.talkself.data.local.user.UserDao
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
            userEntities.map { userEntity ->
                User(
                    name = userEntity.name,
                    conversationId = conversationId,
                    imageUri = userEntity.imageUri,
                    color = userEntity.color,
                    userId = userEntity.userId
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

