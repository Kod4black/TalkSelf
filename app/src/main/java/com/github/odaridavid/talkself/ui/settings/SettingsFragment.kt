package com.github.odaridavid.talkself.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.github.odaridavid.talkself.R

internal class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}
