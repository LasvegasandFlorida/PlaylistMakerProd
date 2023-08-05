package com.example.playlistmakerprod

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchBtn = findViewById<Button>(R.id.search_button)
        val mediaBtn = findViewById<Button>(R.id.media_button)
        val settingsBtn = findViewById<Button>(R.id.settings_button)
        searchBtn.setOnClickListener{
            val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(searchIntent)
        }
        mediaBtn.setOnClickListener{
            val mediaIntent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(mediaIntent)
        }
        settingsBtn.setOnClickListener{
            val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}