package com.github.odaridavid.talkself.common

import android.content.Context
import android.widget.Toast

fun Context.displayToast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}
