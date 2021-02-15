package com.github.odaridavid.talkself.repository

import com.github.odaridavid.talkself.data.room.AppDatabase
import com.github.odaridavid.talkself.data.room.ChatDao
import com.github.odaridavid.talkself.models.Chat
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MainRepository @Inject constructor(private val chatDao: ChatDao) {

    val chats = chatDao.getAllChats()

    suspend fun addChat(chat: Chat){
        chatDao.addChat(chat)
    }

}