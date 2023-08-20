package com.example.playlistmakerprod

import android.content.SharedPreferences

class SearchHistory(private val sharedPrefs: SharedPreferences) {
    fun getSongs(listOfSongs:ArrayList<Track>)
    {
        val tracksPrefs = TracksPreferences()
        listOfSongs.addAll(tracksPrefs.read(sharedPrefs))
    }
    fun add (track:Track)
    {
        val tracksPrefs = TracksPreferences()
        val listOfSongs = tracksPrefs.read(sharedPrefs)
        val sameSong = listOfSongs.find{it.trackId == track.trackId}
        if (sameSong!=null)
            listOfSongs.remove(sameSong)
        else if (listOfSongs.size==10)
            listOfSongs.removeAt(listOfSongs.size-1)
        listOfSongs.add(0,track)
        tracksPrefs.write(sharedPrefs,listOfSongs)
    }
    fun clear()
    {
        sharedPrefs.edit()
            .remove(SEARCH_HISTORY_KEY)
            .apply()
    }
}