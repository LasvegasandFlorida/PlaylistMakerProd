package com.example.playlistmakerprod

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class SongsViewHolder(itemView: View,onItemClick: ((Track) -> Unit)?,tracks: ArrayList<Track>): RecyclerView.ViewHolder(itemView) {

    private val songImage: ImageView = itemView.findViewById(R.id.songImage)
    private val songName: TextView = itemView.findViewById(R.id.songName)
    private val songAuthor: TextView = itemView.findViewById(R.id.songAuthor)
    private val songLength: TextView = itemView.findViewById(R.id.songLength)
init {
    itemView.setOnClickListener {
        onItemClick?.invoke(tracks[adapterPosition])
        val playerIntent = Intent(itemView.context, AudioPlayer::class.java)
        playerIntent.putExtra("trackId",tracks[adapterPosition].trackId)
        playerIntent.putExtra("artistName",tracks[adapterPosition].artistName)
        playerIntent.putExtra("trackName",tracks[adapterPosition].trackName)
        playerIntent.putExtra("trackTime",
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(tracks[adapterPosition].trackTime.toInt()).toString())
        playerIntent.putExtra("trackImage",tracks[adapterPosition].artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
        playerIntent.putExtra("collectionName",tracks[adapterPosition].collectionName)
        playerIntent.putExtra("country",tracks[adapterPosition].country)
        playerIntent.putExtra("primaryGenreName",tracks[adapterPosition].primaryGenreName)
        playerIntent.putExtra("releaseYear", tracks[adapterPosition].releaseDate.substringBefore("-"))
        itemView.context.startActivity(playerIntent)
    }
}
    fun bind(model: Track) {
        songName.text = model.trackName
        songAuthor.text = model.artistName
        songLength.text =SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime.toInt()).toString()
        Glide.with(itemView).load(model.artworkUrl100).centerCrop().transform(RoundedCorners(dpToPx(2f,itemView.context))).placeholder(R.drawable.placeholder).into(songImage)
    }

}