package com.github.odaridavid.talkself.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChat(chat: Chat)

    @Query("SELECT * FROM user where id=:userId ")
    fun getUser(userId : Int): LiveData<List<Chat>>

    @Query("SELECT * FROM user where conversationId =:conversationId  ")
    fun getUsers(conversationId : Int): LiveData<List<Chat>>

    @Query("SELECT * FROM chat order by timeSent asc")
    fun getAllChats(): LiveData<List<Chat>>

    @Query("SELECT * FROM chat WHERE conservationid=:conversationId")
    fun getChats(conversationId: Int): LiveData<List<Chat>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addConversation(conversation: Conversation)

    @Query("SELECT * FROM conversation order by timeCreated asc")
    fun getAllConversations(): LiveData<List<Conversation>>

}