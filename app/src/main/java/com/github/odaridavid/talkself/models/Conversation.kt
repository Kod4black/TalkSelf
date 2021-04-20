package com.github.odaridavid.talkself.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conversation")
data class Conversation(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var timeCreated: Long? = null,
) : Parcelable {

    var lastUser: String? = null
    var lastMessage: String? = null
    var lasttimemessage: Long? = null

    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Long::class.java.classLoader) as? Long
    ) {
        lastUser = parcel.readString()
        lastMessage = parcel.readString()
        lasttimemessage = parcel.readValue(Long::class.java.classLoader) as? Long
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeValue(timeCreated)
        parcel.writeString(lastUser)
        parcel.writeString(lastMessage)
        parcel.writeValue(lasttimemessage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Conversation> {
        override fun createFromParcel(parcel: Parcel): Conversation {
            return Conversation(parcel)
        }

        override fun newArray(size: Int): Array<Conversation?> {
            return arrayOfNulls(size)
        }
    }

}