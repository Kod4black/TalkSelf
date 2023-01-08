package com.github.odaridavid.talkself.data.local.messages

import androidx.room.*
import com.github.odaridavid.talkself.data.local.relations.MessageAndUser
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage(messageEntity: MessageEntity)

    @Transaction
    @Query("SELECT * FROM messages WHERE conversationId=:conversationId order by timeSent asc")
    fun getConversationMessages(conversationId: Int): Flow<List<MessageAndUser>>

    @Query("Delete  FROM messages WHERE conversationId=:conversationId")
    fun deleteMessages(conversationId: Int)

    @Update
    fun updateMessage(messageEntity: MessageEntity)
}
