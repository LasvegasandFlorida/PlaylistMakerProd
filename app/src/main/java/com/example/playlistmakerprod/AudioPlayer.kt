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
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayer : AppCompatActivity() {
    companion object{
        // Key that pass with intent
        var SECOND_ACTIVITY_CODE="second_activity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        val backBtn = findViewById<ImageView>(R.id.player_back_button)
        backBtn.setOnClickListener {
            this.finish()
        }
        var model:Track?=null
        if(intent.hasExtra(SECOND_ACTIVITY_CODE)){
            // getting the Parcelable object into the employeeModel
            model= intent.getParcelableExtra(SECOND_ACTIVITY_CODE)
        }
        if (model != null) {
            val trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime?.toInt()).toString()
            val album = model.collectionName
            val trackTimeView = findViewById<TextView>(R.id.track_time_content)
            trackTimeView.text = trackTime
            val trackAlbumView = findViewById<TextView>(R.id.track_album_content)
            if (!album.isNullOrEmpty()) trackAlbumView.text =album
            else trackAlbumView.visibility = View.GONE
            val trackGenreView = findViewById<TextView>(R.id.track_genre_content)
            trackGenreView.text = model.primaryGenreName
            val trackCountryView = findViewById<TextView>(R.id.track_country_content)
            trackCountryView.text = model.country
            val trackImageView = findViewById<ImageView>(R.id.track_image)
            Glide.with(trackImageView.context).load(model.artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")).centerCrop().transform(RoundedCorners(dpToPx(8f,trackImageView.context))).placeholder(R.drawable.placeholder).into(trackImageView)
            val trackNameView = findViewById<TextView>(R.id.track_name)
            trackNameView.text = model.trackName
            val trackArtistView = findViewById<TextView>(R.id.track_author)
            trackArtistView.text = model.artistName
            val trackTimeNowView = findViewById<TextView>(R.id.track_time_now)
            trackTimeNowView.text = trackTime
            val trackYearView = findViewById<TextView>(R.id.track_year_content)
            trackYearView.text = model.releaseDate?.substringBefore("-")
        }

    }
}