package com.github.odaridavid.talkself.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User

@Database(entities = [Chat::class, Conversation::class,User::class], version = 4,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}