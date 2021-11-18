package com.github.odaridavid.talkself.domain

import com.github.odaridavid.talkself.data.local.models.UserEntity

data class User(
    var name: String? = null,
    val conversationId: Int? = null,
    var imageUri: String? = null,
    var color: String? = null,
    var userId: Int? = null,
)

fun User.toDbEntity(): UserEntity = UserEntity(
    name, conversationId, imageUri, color, userId
)
