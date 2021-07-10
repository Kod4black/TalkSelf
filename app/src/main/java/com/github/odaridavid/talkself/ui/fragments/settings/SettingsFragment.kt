package com.github.odaridavid.talkself.ui.fragments.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.github.odaridavid.talkself.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}