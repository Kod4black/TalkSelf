package com.github.odaridavid.talkself.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.odaridavid.talkself.data.room.relations.ConversationAndUser
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User

@Dao
interface ChatDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addChat(chat: Chat)

    @Query("SELECT * FROM chat order by timeSent asc")
    fun getAllChats(): LiveData<List<Chat>>

    @Query("SELECT * FROM chat WHERE conservationid=:conversationId order by timeSent asc")
    fun getChats(conversationId: Int): LiveData<List<Chat>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun makeConversation(conversation: Conversation)

    @Update
    suspend fun updateConversation(conversation : Conversation)

    @Transaction
    @Query("SELECT * FROM conversation order by timeCreated asc")
    fun getAllConversations(): LiveData<List<Conversation>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun addUser(user: User)

    @Query("SELECT * FROM user where id=:userId ")
    fun getUser(userId : Int): User

    @Query("SELECT * FROM user where conversationId =:conversationId  ")
    fun getUsers(conversationId : Int): LiveData<List<User>>

    @Delete
    fun deleteConversation(conversation: Conversation)

    @Query("Delete  FROM chat WHERE conservationid=:conversationId")
    fun deleteChats(conversationId: Int)

    @Update
    fun updateChat(chat: Chat)

}