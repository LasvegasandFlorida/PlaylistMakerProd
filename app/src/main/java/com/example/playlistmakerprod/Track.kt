package com.example.playlistmakerprod

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Track(val trackId:Int, val trackName:String, val artistName: String, @SerializedName("trackTimeMillis") val trackTime: String, val artworkUrl100: String,val country:String, val primaryGenreName:String,val collectionName:String, val releaseDate:String)
