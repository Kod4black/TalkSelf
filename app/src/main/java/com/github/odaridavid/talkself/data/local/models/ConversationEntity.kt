package com.github.odaridavid.talkself.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.odaridavid.talkself.ui.models.ConversationUiModel

@Entity(tableName = "conversation")
data class ConversationEntity(
    @PrimaryKey(autoGenerate = false)
    var conversationId: Int? = null,
    var timeCreated: Long? = null,
    var userId: Int? = null,
    var lastMessage: String? = null,
    var lasttimemessage: Long? = null,
)

fun ConversationEntity.toUiModel():ConversationUiModel = ConversationUiModel(
    conversationId, timeCreated, userId, lastMessage, lasttimemessage
)
