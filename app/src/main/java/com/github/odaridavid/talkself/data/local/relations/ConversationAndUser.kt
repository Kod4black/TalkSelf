package com.github.odaridavid.talkself.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.github.odaridavid.talkself.data.local.models.ConversationEntity
import com.github.odaridavid.talkself.data.local.models.UserEntity

data class ConversationAndUser(
    @Embedded
    val conversationEntity: ConversationEntity?,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val userEntity: UserEntity?
)
