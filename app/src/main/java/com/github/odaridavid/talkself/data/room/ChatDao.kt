package com.github.odaridavid.talkself.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Query
import androidx.room.Transaction
import com.github.odaridavid.talkself.data.room.relations.ChatAndUser
import com.github.odaridavid.talkself.data.room.relations.ConversationAndUser
import com.github.odaridavid.talkself.models.Chat
import kotlinx.coroutines.flow.Flow

// TODO Rename references of chat to messages
//TODO Redesign db and table relations for user,conversations they are in and messages attached to conversations
@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChat(chat: Chat)

    @Query("SELECT * FROM chat order by timeSent asc")
    fun getAllChats(): Flow<List<Chat>>

    //TODO Check why is this a transaction
    @Transaction
    @Query("SELECT * FROM chat WHERE conservationid=:conversationId order by timeSent asc")
    fun getChats(conversationId: Int): Flow<List<ChatAndUser>>

    @Query("Delete  FROM chat WHERE conservationid=:conversationId")
    fun deleteChats(conversationId: Int)

    @Update
    fun updateChat(chat: Chat)

}
