package com.github.odaridavid.talkself.data.room

import androidx.room.*
import com.github.odaridavid.talkself.data.room.relations.ConversationAndUser
import com.github.odaridavid.talkself.models.Conversation
import kotlinx.coroutines.flow.Flow

@Dao
interface ConversationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConversation(conversation: Conversation)

    @Update
    suspend fun updateConversation(conversation: Conversation)

    @Query("SELECT * FROM conversation order by timeCreated asc")
    fun getAllConversations(): Flow<List<Conversation>>

    @Delete
    fun deleteConversation(conversation: Conversation)

    //TODO Check why is this a transaction
    @Transaction
    @Query("SELECT * FROM conversation order by timeCreated asc")
    fun getAllConversationsAndUsers(): Flow<List<ConversationAndUser>>
}
