package com.github.odaridavid.talkself.common

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

object TimeUtils {
    @SuppressLint("SimpleDateFormat")
    fun formatMillisecondsToHrsAndMinutes(createdAt: Long): String {
        val format: DateFormat = SimpleDateFormat("HH:mm a")
        format.timeZone = TimeZone.getDefault()
        return format.format(createdAt)
    }

    @SuppressLint("SimpleDateFormat")
    fun formatMillisecondsToDate(createdAt: Long): String {
        val format: DateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm a")
        format.timeZone = TimeZone.getDefault()
        return format.format(createdAt)
    }
}
