package com.example.playlistmakerprod

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
    }
}
    fun bind(model: Track) {
        songName.text = model.trackName
        songAuthor.text = model.artistName
        songLength.text =SimpleDateFormat("mm:ss", Locale.getDefault()).format(model.trackTime.toInt()).toString()
        Glide.with(itemView).load(model.artworkUrl100).centerCrop().transform(RoundedCorners(dpToPx(2f,SearchActivity.applicationContext()))).placeholder(R.drawable.placeholder).into(songImage)
    }

}