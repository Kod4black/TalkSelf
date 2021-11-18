package com.github.odaridavid.talkself.domain

import com.github.odaridavid.talkself.data.local.models.ConversationEntity

data class Conversation(
    var conversationId: Int? = null,
    var timeCreated: Long? = null,
    var userId: Int? = null,
    var lastMessage: String? = null,
    var lasttimemessage: Long? = null,
)

fun Conversation.toDbEntity() = ConversationEntity(
    conversationId = conversationId,
    timeCreated = timeCreated,
    userId = userId,
    lastMessage = lastMessage,
    lasttimemessage = lasttimemessage
)
