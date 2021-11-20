package com.github.odaridavid.talkself.data.local.conversation

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.OnConflictStrategy
import com.github.odaridavid.talkself.data.local.relations.ConversationAndUser
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversationEntity: ConversationEntity)

    @Update
    suspend fun updateConversation(conversationEntity: ConversationEntity)

    @Query("SELECT * FROM conversation order by timeCreated asc")
    fun getAllConversations(): Flow<List<ConversationEntity>>

    @Delete
    fun deleteConversation(conversationEntity: ConversationEntity)

    @Query("SELECT * FROM conversation order by timeCreated asc")
    fun getAllConversationsAndUsers(): Flow<List<ConversationAndUser>>
}
