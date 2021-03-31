package com.github.odaridavid.talkself.di

import com.github.odaridavid.talkself.data.room.AppDatabase
import com.github.odaridavid.talkself.data.room.ChatDao
import com.github.odaridavid.talkself.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Inject
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainrepository(chatDao: ChatDao): MainRepository {
        return MainRepository(chatDao)
    }


}