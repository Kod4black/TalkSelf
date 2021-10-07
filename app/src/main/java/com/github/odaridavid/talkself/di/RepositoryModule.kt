package com.github.odaridavid.talkself.di

import com.github.odaridavid.talkself.data.room.ChatDao
import com.github.odaridavid.talkself.data.room.ConversationDao
import com.github.odaridavid.talkself.data.room.UserDao
import com.github.odaridavid.talkself.repository.MessagesRepository
import com.github.odaridavid.talkself.repository.ConversationRepository
import com.github.odaridavid.talkself.repository.UserRepository
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
        conversationDao: ConversationDao,
        dispatcher: CoroutineDispatcher
    ): ConversationRepository =
        ConversationRepository(conversationDao = conversationDao, dispatcher = dispatcher)

    @Provides
    @Singleton
    fun providesUserRepository(
        userDao: UserDao,
        dispatcher: CoroutineDispatcher
    ): UserRepository =
        UserRepository(userDao = userDao, dispatcher = dispatcher)

    @Provides
    @Singleton
    fun providesDispatcher(): CoroutineDispatcher = Dispatchers.IO

}
