package com.github.odaridavid.talkself.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.odaridavid.talkself.data.local.models.ChatEntity
import com.github.odaridavid.talkself.data.local.models.ConversationEntity
import com.github.odaridavid.talkself.data.local.models.UserEntity

@Database(entities = [ChatEntity::class, ConversationEntity::class, UserEntity::class], version = 13,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
    abstract fun conversationDao(): ConversationDao
    abstract fun userDao(): UserDao
}
