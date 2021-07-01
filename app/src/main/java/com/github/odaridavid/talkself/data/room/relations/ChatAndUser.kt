package com.github.odaridavid.talkself.data.room.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.github.odaridavid.talkself.models.Chat
import com.github.odaridavid.talkself.models.User

data class ChatAndUser(
    @Embedded
    var chat: Chat,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val user: User
)