package com.github.odaridavid.talkself.di

import android.app.Application
import androidx.room.Room
import com.github.odaridavid.talkself.data.local.AppDatabase
import com.github.odaridavid.talkself.data.local.ChatDao
import com.github.odaridavid.talkself.data.local.ConversationDao
import com.github.odaridavid.talkself.data.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    //TODO Write migrations and support exporting schemas after fixing db structure and queries
    @Provides
    @Singleton
    fun providesAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "self.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesChatDao(appDatabase: AppDatabase): ChatDao =
        appDatabase.chatDao()

    @Provides
    @Singleton
    fun providesUserDao(appDatabase: AppDatabase): UserDao =
        appDatabase.userDao()

    @Provides
    @Singleton
    fun providesConversationDao(appDatabase: AppDatabase): ConversationDao =
        appDatabase.conversationDao()
}
