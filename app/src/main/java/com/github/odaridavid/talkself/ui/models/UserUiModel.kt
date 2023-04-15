package com.github.odaridavid.talkself.ui.models

import android.os.Parcelable
import com.github.odaridavid.talkself.data.local.user.UserEntity
import com.github.odaridavid.talkself.domain.models.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserUiModel(
    var name: String? = null,
    val conversationId: Int? = null,
    var imageUri: String? = null,
    var color: String? = null,
    var userId: Int? = null,
) : Parcelable

fun UserUiModel.toDomain(): User = User(
    name, conversationId, imageUri, color, userId
)

// TODO Strange should only map to domain
fun UserUiModel.toDbEntity(): UserEntity = UserEntity(
    name, conversationId, imageUri, color, userId
)
