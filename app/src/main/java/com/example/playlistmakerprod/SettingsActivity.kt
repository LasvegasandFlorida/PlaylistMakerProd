package com.example.playlistmakerprod

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatDelegate

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val backBtn = findViewById<ImageView>(R.id.back_button)
        backBtn.setOnClickListener {
            this.finish()
        }
        val themeSwitch = findViewById<Switch>(R.id.theme_switch)
        when (AppCompatDelegate.getDefaultNightMode())
        {
            AppCompatDelegate.MODE_NIGHT_YES -> themeSwitch.isChecked = true
            AppCompatDelegate.MODE_NIGHT_NO -> themeSwitch.isChecked = false
        }
        themeSwitch.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }
        val shareBtn = findViewById<Button>(R.id.share_button)
        shareBtn.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_link))
            val sendIntent = Intent.createChooser(shareIntent, null)
            startActivity(sendIntent)
        }
        val supportBtn = findViewById<Button>(R.id.support_button)
        supportBtn.setOnClickListener {
            startActivity(Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_subject))
                putExtra(Intent.EXTRA_EMAIL, arrayOf(getString(R.string.my_email)))
                putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_body))
            }
            )
        }
        val agreementBtn = findViewById<Button>(R.id.agreement_button)
        agreementBtn.setOnClickListener {
            val url = Uri.parse(getString(R.string.offer_link))
            val agreementIntent = Intent(Intent.ACTION_VIEW, url)
            startActivity(agreementIntent)
        }

    }
}