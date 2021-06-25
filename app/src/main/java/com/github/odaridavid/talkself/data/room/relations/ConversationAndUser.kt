package com.github.odaridavid.talkself.data.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.github.odaridavid.talkself.models.Conversation
import com.github.odaridavid.talkself.models.User

data class ConversationAndUser (
    @Embedded val conversation: Conversation,
    @Relation(
        parentColumn = "conversationId",
        entityColumn = "conversationId"
    )
    val user: User
    )