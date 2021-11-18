package com.github.odaridavid.talkself.ui.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatUiModel(
    var chatId: Int? = null,
    var userId: Int? = null,
    var username: String? = null,
    var message: String? = null,
    var timesent: Long? = null,
    var conservationId: Int? = null,
) : Parcelable
