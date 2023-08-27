package com.example.playlistmakerprod

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.transition.Visibility
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

class AudioPlayer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        val backBtn = findViewById<ImageView>(R.id.player_back_button)
        backBtn.setOnClickListener {
            this.finish()
        }
        val album = intent.getStringExtra("collectionName")
        val trackTimeView = findViewById<TextView>(R.id.track_time_content)
        trackTimeView.text = intent.getStringExtra("trackTime")
        val trackAlbumView = findViewById<TextView>(R.id.track_album_content)
        if (!album.isNullOrEmpty()) trackAlbumView.text =album
        else trackAlbumView.visibility = View.GONE
        val trackGenreView = findViewById<TextView>(R.id.track_genre_content)
        trackGenreView.text = intent.getStringExtra("primaryGenreName")
        val trackCountryView = findViewById<TextView>(R.id.track_country_content)
        trackCountryView.text = intent.getStringExtra("country")
        val trackImageView = findViewById<ImageView>(R.id.track_image)
        Glide.with(trackImageView.context).load(intent.getStringExtra("trackImage")).centerCrop().transform(RoundedCorners(dpToPx(8f,trackImageView.context))).placeholder(R.drawable.placeholder).into(trackImageView)
        val trackNameView = findViewById<TextView>(R.id.track_name)
        trackNameView.text = intent.getStringExtra("trackName")
        val trackArtistView = findViewById<TextView>(R.id.track_author)
        trackArtistView.text = intent.getStringExtra("artistName")
        val trackTimeNowView = findViewById<TextView>(R.id.track_time_now)
        trackTimeNowView.text = intent.getStringExtra("trackTime")
        val trackYearView = findViewById<TextView>(R.id.track_year_content)
        trackYearView.text = intent.getStringExtra("releaseYear")
    }
}