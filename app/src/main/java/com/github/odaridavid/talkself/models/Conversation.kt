package com.github.odaridavid.talkself.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "conversation")
@Parcelize
data class Conversation (
    @PrimaryKey(autoGenerate = false)
    var id : Int,
    val timeCreated: Long? = null,
    var lastMessage : String? = null,
    var lastUserId: Int? = null
) : Parcelable