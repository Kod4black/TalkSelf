package com.github.odaridavid.talkself.data.local.conversation

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.odaridavid.talkself.domain.models.Conversation

@Entity(tableName = "conversation")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = false)
    var conversationId: Int? = null,
    var timeCreated: Long? = null,
    var userId: Int? = null,
    var lastMessage: String? = null,
    var lasttimemessage: Long? = null,
)

fun ConversationEntity.toDomain(): Conversation = Conversation(
    conversationId = conversationId,
    timeCreated = timeCreated,
    userId = userId,
    lastMessage = lastMessage,
    lasttimemessage = lasttimemessage
)
