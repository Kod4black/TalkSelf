package com.github.odaridavid.talkself.data

import com.github.odaridavid.talkself.data.local.user.UserDao
import com.github.odaridavid.talkself.domain.models.User
import com.github.odaridavid.talkself.domain.models.toDbEntity
import com.github.odaridavid.talkself.domain.repository.UserRepository
import com.github.odaridavid.talkself.ui.models.UserUiModel
import com.github.odaridavid.talkself.ui.models.toDbEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository{

   override fun getUsersInConversation(conversationId: Int): Flow<List<User>> =
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

    override fun addUser(user: User) {
        val userEntity = user.toDbEntity()
        userDao.addUser(userEntity)
    }

    override suspend fun updateUser(userUiModel: UserUiModel) {
        val userEntity = userUiModel.toDbEntity()
        userDao.updateUser(userEntity)
    }
}

