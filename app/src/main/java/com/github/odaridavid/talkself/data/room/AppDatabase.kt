package com.github.odaridavid.talkself.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.github.odaridavid.talkself.models.Chat

@Database(entities = [Chat::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chatDao(): ChatDao
}