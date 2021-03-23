package com.github.odaridavid.talkself.repository

import com.github.odaridavid.talkself.data.room.ChatDao
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User
import javax.inject.Inject

class MainRepository @Inject constructor(private val chatDao: ChatDao) {

    val chats = chatDao.getAllChats()
    val conversations = chatDao.getAllConversations()
    fun users(conversationid : Int) = chatDao.getUsers(conversationid)

    fun chats(conversationid : Int) = chatDao.getChats(conversationid)

    suspend fun addChat(chat: Chat){
        chatDao.addChat(chat)
    }

    fun addUser(user: User){
        chatDao.addUser(user)
    }

    suspend fun addconversation(conversation: Conversation){
        chatDao.makeConversation(conversation )
    }

}