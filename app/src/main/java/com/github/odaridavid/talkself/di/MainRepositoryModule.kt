package com.github.odaridavid.talkself.di

import com.github.odaridavid.talkself.data.room.AppDatabase
import com.github.odaridavid.talkself.data.room.ChatDao
import com.github.odaridavid.talkself.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainrepository(chatDao: ChatDao): MainRepository {
        return MainRepository(chatDao)
    }


}