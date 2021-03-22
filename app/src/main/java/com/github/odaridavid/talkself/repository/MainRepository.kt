package com.github.odaridavid.talkself.repository

import com.github.odaridavid.talkself.data.room.ChatDao
import com.github.odaridavid.talkself.models.Chat
import javax.inject.Inject

class MainRepository @Inject constructor(private val chatDao: ChatDao) {

    val chats = chatDao.getAllChats()
    val conversations = chatDao.getAllConversations()
    fun users(conversationid : Int) = chatDao.getUsers(conversationid)

    suspend fun addChat(chat: Chat){
        chatDao.addChat(chat)
    }


}