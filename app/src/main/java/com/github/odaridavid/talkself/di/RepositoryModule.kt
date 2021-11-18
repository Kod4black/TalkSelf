package com.github.odaridavid.talkself.di

import com.github.odaridavid.talkself.data.local.ChatDao
import com.github.odaridavid.talkself.data.local.ConversationDao
import com.github.odaridavid.talkself.data.local.UserDao
import com.github.odaridavid.talkself.data.repository.MessagesRepository
import com.github.odaridavid.talkself.data.repository.ConversationRepository
import com.github.odaridavid.talkself.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

//TODO Refactor di module when package structure changes to grouping by feature as opposed to by layer
@InstallIn(SingletonComponent::class)
@Module
internal object RepositoryModule {

    @Provides
    @Singleton
    fun providesChatRepository(
        chatDao: ChatDao,
        dispatcher: CoroutineDispatcher
    ): MessagesRepository =
        MessagesRepository(chatDao = chatDao, dispatcher = dispatcher)

    @Provides
    @Singleton
    fun providesConversationRepository(
        conversationDao: ConversationDao
    ): ConversationRepository =
        ConversationRepository(conversationDao = conversationDao)

    @Provides
    @Singleton
    fun providesUserRepository(
        userDao: UserDao
    ): UserRepository =
        UserRepository(userDao = userDao)

    @Provides
    @Singleton
    fun providesDispatcher(): CoroutineDispatcher = Dispatchers.IO

}
