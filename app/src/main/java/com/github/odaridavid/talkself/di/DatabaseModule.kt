package com.github.odaridavid.talkself.di

import android.app.Application
import androidx.room.Room
import com.github.odaridavid.talkself.data.local.AppDatabase
import com.github.odaridavid.talkself.data.local.MIGRATION_13_14
import com.github.odaridavid.talkself.data.local.conversation.ConversationDao
import com.github.odaridavid.talkself.data.local.messages.MessagesDao
import com.github.odaridavid.talkself.data.local.user.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    @Singleton
    fun providesAppDatabase(application: Application): AppDatabase =
        Room.databaseBuilder(application, AppDatabase::class.java, "self.db")
            .addMigrations(MIGRATION_13_14)
            .build()


    @Provides
    @Singleton
    fun providesMessagesDao(appDatabase: AppDatabase): MessagesDao =
        appDatabase.messagesDao()

    @Provides
    @Singleton
    fun providesUserDao(appDatabase: AppDatabase): UserDao =
        appDatabase.userDao()

    @Provides
    @Singleton
    fun providesConversationDao(appDatabase: AppDatabase): ConversationDao =
        appDatabase.conversationDao()
}
