package com.github.odaridavid.talkself.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "conversation")
@Parcelize
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val timeCreated: Long? = null,
) : Parcelable {

    var lastUserId: Int? = null
    var lastMessage: String? = null

}