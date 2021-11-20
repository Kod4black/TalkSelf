package com.github.odaridavid.talkself.data.local.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(userEntity: UserEntity)

    @Update
    suspend fun updateUser(userEntity: UserEntity)

    @Query("SELECT * FROM user where userId=:userId ")
    fun getUser(userId: Int): UserEntity

    @Query("SELECT * FROM user where conversationId =:conversationId  ")
    fun getUsers(conversationId: Int): Flow<List<UserEntity>>
}
