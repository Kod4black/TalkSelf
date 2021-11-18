package com.github.odaridavid.talkself.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.github.odaridavid.talkself.data.local.models.ChatEntity
import com.github.odaridavid.talkself.data.local.models.UserEntity

data class ChatAndUser(
    @Embedded
    var chatEntity: ChatEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val userEntity: UserEntity
)
