package com.example.playlistmakerprod

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backBtn = findViewById<ImageView>(R.id.back_button)
        backBtn.setOnClickListener {
            this.finish()
        }
    }
}