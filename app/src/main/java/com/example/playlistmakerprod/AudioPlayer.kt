package com.example.playlistmakerprod

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
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
        private const val UPDATE_TIME_DELAY = 500L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var playBtn: ImageView
    private lateinit var trackProgress: TextView
    private var mainThreadHandler: Handler? = null
    private var url:String? = ""
    private fun preparePlayer() {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playBtn.isEnabled = true
            playBtn.setImageDrawable(getDrawable(R.drawable.play_btn))
            trackProgress.setText("00:00")
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playBtn.setImageDrawable(getDrawable(R.drawable.play_btn))
            trackProgress.setText("00:00")
            playerState = STATE_PREPARED
            mainThreadHandler?.removeCallbacksAndMessages(null)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        val backBtn = findViewById<ImageView>(R.id.player_back_button)
        backBtn.setOnClickListener {
            this.finish()
        }
        mainThreadHandler = Handler(Looper.getMainLooper())
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
            url = model.previewUrl
        }
        playBtn = findViewById<ImageView>(R.id.play_button)
        preparePlayer()

        trackProgress = findViewById<TextView>(R.id.track_time_now)
    }

    override fun onStart() {
        super.onStart()
        playBtn.setOnClickListener {
            playbackControl()
        }
    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }
    private fun startPlayer() {
        mediaPlayer.start()
        playBtn.setImageDrawable(getDrawable(R.drawable.pause_btn))
        playerState = STATE_PLAYING
        mainThreadHandler?.postDelayed(
            object : Runnable {
                override fun run() {
                    checkTime()
                    mainThreadHandler?.postDelayed(
                        this,
                        UPDATE_TIME_DELAY,
                    )
                }
            },
            UPDATE_TIME_DELAY
        )
    }

    private fun pausePlayer() {

        mediaPlayer.pause()
        playBtn.setImageDrawable(getDrawable(R.drawable.play_btn))
        playerState = STATE_PAUSED
        mainThreadHandler?.removeCallbacksAndMessages(null)
    }
    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }
    private fun checkTime() {
        if (playerState == STATE_PLAYING){
            var currentTime = 30000L- mediaPlayer.currentPosition.toLong()
            trackProgress.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTime).toString()
        }
    }
}