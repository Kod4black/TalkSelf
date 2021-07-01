package com.github.odaridavid.talkself.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "conversation")
@Parcelize
data class Conversation(
    @PrimaryKey(autoGenerate = false)
    var conversationId: Int? = null,
    var timeCreated: Long? = null,
    var userId: Int? = null,
    var lastMessage: String? = null,
    var lasttimemessage: Long? = null,
) : Parcelable