package com.github.odaridavid.talkself.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.odaridavid.talkself.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}