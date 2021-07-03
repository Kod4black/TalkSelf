package com.github.odaridavid.talkself.repository

import com.github.odaridavid.talkself.data.room.ChatDao
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import javax.inject.Inject

class MainRepository @Inject constructor(private val chatDao: ChatDao) {

    val chats = chatDao.getAllChats()
    val conversations = chatDao.getAllConversations()
    val conversationsandusers = chatDao.getAllConversationsandUsers()

    fun users(conversationId : Int) = chatDao.getUsers(conversationId)

    fun chats(conversationId : Int) = chatDao.getChats(conversationId)

    suspend fun addChat(chat: Chat){
        chatDao.addChat(chat)
    }

    fun addUser(user: User){
        chatDao.addUser(user)
    }

    fun getUser(userId : Int) : User{
        return chatDao.getUser(userId)
    }

    suspend fun updateUser(user: User){
        chatDao.updateUser(user)
    }

    suspend fun addConversation(conversation: Conversation){
        chatDao.makeConversation(conversation )
    }

    suspend fun updateConversation(conversation: Conversation){
        chatDao.updateConversation(conversation )
    }

     suspend fun deleteConversation(conversation: Conversation){
         chatDao.deleteConversation(conversation)
    }


    fun deleteChats(conversation: Conversation){
        chatDao.deleteChats(conversation.conversationId!!)
    }

    fun updateChat(chat: Chat) {
        chatDao.updateChat(chat)
    }


}