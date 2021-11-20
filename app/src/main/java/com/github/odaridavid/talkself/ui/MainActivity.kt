package com.github.odaridavid.talkself.ui

import android.Manifest.permission.*
import android.app.UiModeManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.odaridavid.talkself.R
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    lateinit var uiManager: UiModeManager
    private var mFirebaseAnalytics: FirebaseAnalytics? = null

    // TODO Read this from sp
    private var hasOptedIntoAnalytics = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uiManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        if (hasOptedIntoAnalytics && hasNecessaryPermissions()) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this@MainActivity)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == "theme") {
            val state = sharedPreferences?.getBoolean(key, true)!!
            if (state) {
                uiManager.nightMode = UiModeManager.MODE_NIGHT_YES
            } else {
                uiManager.nightMode = UiModeManager.MODE_NIGHT_NO
            }
        }
    }

    private fun hasNecessaryPermissions(): Boolean =
        ContextCompat.checkSelfPermission(this, ACCESS_NETWORK_STATE) == PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, WAKE_LOCK) == PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, INTERNET) == PERMISSION_GRANTED

}


