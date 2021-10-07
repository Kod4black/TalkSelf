package com.github.odaridavid.talkself.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Query
import com.github.odaridavid.talkself.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user where userId=:userId ")
    fun getUser(userId: Int): User

    @Query("SELECT * FROM user where conversationId =:conversationId  ")
    fun getUsers(conversationId: Int): Flow<List<User>>
}
