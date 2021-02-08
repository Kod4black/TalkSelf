package com.github.odaridavid.talkself.utils

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    companion object{
        @SuppressLint("SimpleDateFormat")
        fun formatMillisecondsToTime(createdAt: Long): String {
            val format: DateFormat = SimpleDateFormat("HH:mm:ss")
            format.timeZone = TimeZone.getDefault()
            return format.format(createdAt)
        }

    }

}
