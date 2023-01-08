package com.github.odaridavid.talkself.data.local.messages

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.odaridavid.talkself.domain.Message

@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    var chatId: Int? = null,
    var userId: Int? = null,
    var username: String? = null,
    var message: String? = null,
    var timesent: Long? = null,
    var conversationId: Int? = null,
)

fun MessageEntity.toDomain():Message = Message(
    chatId, userId, username, message, timesent, conversationId
)
