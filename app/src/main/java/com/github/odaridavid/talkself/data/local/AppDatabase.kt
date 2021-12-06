package com.github.odaridavid.talkself.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.odaridavid.talkself.data.local.conversation.ConversationDao
import com.github.odaridavid.talkself.data.local.conversation.ConversationEntity
import com.github.odaridavid.talkself.data.local.messages.MessageEntity
import com.github.odaridavid.talkself.data.local.messages.MessagesDao
import com.github.odaridavid.talkself.data.local.user.UserDao
import com.github.odaridavid.talkself.data.local.user.UserEntity

@Database(
    entities = [MessageEntity::class, ConversationEntity::class, UserEntity::class],
    version = 14,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun messagesDao(): MessagesDao
    abstract fun conversationDao(): ConversationDao
    abstract fun userDao(): UserDao
}
