package com.github.odaridavid.talkself.di

import com.github.odaridavid.talkself.data.local.messages.MessagesDao
import com.github.odaridavid.talkself.data.local.conversation.ConversationDao
import com.github.odaridavid.talkself.data.local.user.UserDao
import com.github.odaridavid.talkself.data.MessagesRepository
import com.github.odaridavid.talkself.data.ConversationRepository
import com.github.odaridavid.talkself.data.UserRepository
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
        messagesDao: MessagesDao
    ): MessagesRepository =
        MessagesRepository(messagesDao = messagesDao)

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
