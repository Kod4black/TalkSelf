package com.github.odaridavid.talkself.di

import android.app.Application
import androidx.room.Room
import com.github.odaridavid.talkself.data.room.AppDatabase
import com.github.odaridavid.talkself.data.room.ChatDao
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
    fun providesAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "self.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun providesChatDao(appDatabase: AppDatabase): ChatDao {
        return appDatabase.chatDao()
    }


}