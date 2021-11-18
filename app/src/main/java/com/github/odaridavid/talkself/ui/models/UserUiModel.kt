package com.github.odaridavid.talkself.ui.models

import android.os.Parcelable
import com.github.odaridavid.talkself.data.local.models.UserEntity
import com.github.odaridavid.talkself.domain.User
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
fun UserUiModel.toDbEntity(): UserEntity = UserEntity(
    name, conversationId, imageUri, color, userId
)
