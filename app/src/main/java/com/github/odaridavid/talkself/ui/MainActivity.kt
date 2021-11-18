package com.github.odaridavid.talkself.ui

import android.app.UiModeManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.odaridavid.talkself.R
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    lateinit var uiManager: UiModeManager
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uiManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this)

    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "theme") {
            val state = sharedPreferences?.getBoolean(key, true)!!
            if (state){
                uiManager.nightMode = UiModeManager.MODE_NIGHT_YES
            }else{
                uiManager.nightMode = UiModeManager.MODE_NIGHT_NO
            }
        }
    }
}


