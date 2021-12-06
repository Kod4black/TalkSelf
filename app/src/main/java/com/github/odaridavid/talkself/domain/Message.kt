package com.github.odaridavid.talkself.domain

import com.github.odaridavid.talkself.data.local.messages.MessageEntity
import com.github.odaridavid.talkself.ui.models.MessageUiModel

data class Message(
    var chatId: Int? = null,
    var userId: Int? = null,
    var username: String? = null,
    var message: String? = null,
    var timesent: Long? = null,
    var conservationId: Int? = null,
)

fun Message.toDbEntity(): MessageEntity =
    MessageEntity(chatId, userId, username, message, timesent, conservationId)

fun Message.toUiModel(): MessageUiModel =
    MessageUiModel(chatId, userId, username, message, timesent, conservationId)
