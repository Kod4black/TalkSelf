package com.github.odaridavid.talkself.data.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.github.odaridavid.talkself.data.room.relations.ChatAndUser
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

    @Transaction
    @Query("SELECT * FROM chat WHERE conservationid=:conversationId order by timeSent asc")
    fun getChats(conversationId: Int): LiveData<List<ChatAndUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun makeConversation(conversation: Conversation)

    @Update
    suspend fun updateConversation(conversation : Conversation)

    @Query("SELECT * FROM conversation order by timeCreated asc")
    fun getAllConversations(): LiveData<List<Conversation>>

    @Transaction
    @Query("SELECT * FROM conversation order by timeCreated asc")
    fun getAllConversationsandUsers(): LiveData<List<ConversationAndUser>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user where userId=:userId ")
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