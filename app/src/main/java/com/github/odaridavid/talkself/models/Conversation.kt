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
    var timeCreated: Long? = null,
) : Parcelable {

    var lastUser: String? = null
    var lastMessage: String? = null
    var lasttimemessage: Long? = null

}