package com.github.odaridavid.talkself.ui.models

import android.os.Parcelable
import com.github.odaridavid.talkself.domain.models.Message
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageUiModel(
    var chatId: Int? = null,
    var userId: Int? = null,
    var username: String? = null,
    var message: String? = null,
    var timesent: Long? = null,
    var conservationId: Int? = null,
) : Parcelable

fun MessageUiModel.toDomain(): Message =
    Message(chatId, userId, username, message, timesent, conservationId)
