package com.github.odaridavid.talkself.data.local.messages

import androidx.room.*
import com.github.odaridavid.talkself.data.local.relations.MessageAndUser
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMessage(messageEntity: MessageEntity)


    @Query("Delete  FROM messages WHERE conservationid=:conversationId")
    fun deleteMessages(conversationId: Int)

    @Update
    fun updateMessage(messageEntity: MessageEntity)

    @Transaction
    @Query("SELECT * FROM messages WHERE conservationid=:conversationId order by timeSent asc")
    fun getConversationMessages(conversationId: Int): Flow<List<MessageAndUser>>

}
