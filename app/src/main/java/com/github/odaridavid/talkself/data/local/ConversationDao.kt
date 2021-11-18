package com.github.odaridavid.talkself.data.local

import androidx.room.*
import com.github.odaridavid.talkself.data.local.relations.ConversationAndUser
import com.github.odaridavid.talkself.data.local.models.ConversationEntity
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

    //TODO Check why is this a transaction
    @Transaction
    @Query("SELECT * FROM conversation order by timeCreated asc")
    fun getAllConversationsAndUsers(): Flow<List<ConversationAndUser>>
}
