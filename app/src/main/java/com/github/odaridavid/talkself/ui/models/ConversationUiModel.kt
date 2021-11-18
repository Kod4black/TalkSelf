package com.github.odaridavid.talkself.ui.models

import android.os.Parcelable
import com.github.odaridavid.talkself.domain.Conversation
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConversationUiModel(
    var conversationId: Int? = null,
    var timeCreated: Long? = null,
    var userId: Int? = null,
    var lastMessage: String? = null,
    var lasttimemessage: Long? = null,
) : Parcelable


fun ConversationUiModel.toDomain(): Conversation = Conversation(
    conversationId = conversationId,
    timeCreated = timeCreated,
    userId = userId,
    lastMessage = lastMessage,
    lasttimemessage = lasttimemessage
)

