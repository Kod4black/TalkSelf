package com.github.odaridavid.talkself.repository

import com.github.odaridavid.talkself.data.room.ChatDao
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import com.github.odaridavid.talkself.utils.Coroutines
import javax.inject.Inject

class MainRepository @Inject constructor(private val chatDao: ChatDao) {

    val chats = chatDao.getAllChats()
    val conversations = chatDao.getAllConversations()

    fun users(conversationId : Int) = chatDao.getUsers(conversationId)

    fun chats(conversationId : Int) = chatDao.getChats(conversationId)

    suspend fun addChat(chat: Chat){
        chatDao.addChat(chat)
    }

    fun addUser(user: User){
        chatDao.addUser(user)
    }

    suspend fun getUser(userId : Int) : User{
        return chatDao.getUser(userId)
    }

    suspend fun addconversation(conversation: Conversation){
        chatDao.makeConversation(conversation )
    }

    suspend fun updateconversation(conversation: Conversation){
        chatDao.updateConversation(conversation )
    }

     suspend fun deleteConversation(conversation: Conversation){
         Coroutines.io {
             chatDao.deleteConversation(conversation)
         }
    }

    fun deleteChats(conversation: Conversation){
        chatDao.deleteChats(conversation.conservationId!!)
    }

    fun updateChat(chat: Chat) {
        chatDao.updateChat(chat)
    }


}