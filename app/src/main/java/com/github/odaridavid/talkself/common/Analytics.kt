package com.github.odaridavid.talkself.common

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object Analytics {
    fun instance(): FirebaseAnalytics = Firebase.analytics
}
