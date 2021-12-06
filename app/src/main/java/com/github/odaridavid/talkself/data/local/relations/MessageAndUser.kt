package com.github.odaridavid.talkself.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.github.odaridavid.talkself.data.local.messages.MessageEntity
import com.github.odaridavid.talkself.data.local.user.UserEntity

// TODO Decouple this relation
data class MessageAndUser(
    @Embedded
    var messageEntity: MessageEntity,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val userEntity: UserEntity
)
