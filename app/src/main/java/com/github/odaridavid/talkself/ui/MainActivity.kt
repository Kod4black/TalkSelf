package com.github.odaridavid.talkself.ui

import android.app.UiModeManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.odaridavid.talkself.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity :
    AppCompatActivity(R.layout.activity_main),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var uiManager: UiModeManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uiManager = getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
    }

    // TODO Cater for Power Saving mode when working with dark themes
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == getString(R.string.pref_key_theme)) {
            val isNightThemeEnabled = sharedPreferences?.getBoolean(key, true)!!
            if (isNightThemeEnabled) {
                uiManager.nightMode = UiModeManager.MODE_NIGHT_YES
            } else {
                uiManager.nightMode = UiModeManager.MODE_NIGHT_NO
            }
        }
    }
}


