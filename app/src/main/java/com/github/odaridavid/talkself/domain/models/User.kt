package com.github.odaridavid.talkself.domain.models

import com.github.odaridavid.talkself.data.local.user.UserEntity
import com.github.odaridavid.talkself.ui.models.UserUiModel

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

fun User.toUiModel(): UserUiModel = UserUiModel(
    name, conversationId, imageUri, color, userId
)
