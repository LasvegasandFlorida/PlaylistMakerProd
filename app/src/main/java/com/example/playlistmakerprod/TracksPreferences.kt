package com.example.playlistmakerprod

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken




class TracksPreferences {
    fun read(sharedPreferences: SharedPreferences): ArrayList<Track>? {
        val json = sharedPreferences.getString(SEARCH_HISTORY_KEY, null) ?: return arrayListOf()
        return Gson().fromJson(json, object : TypeToken<ArrayList<Track?>?>(){}.type)
    }

    fun write(sharedPreferences: SharedPreferences, tracks: ArrayList<Track>) {
        val json = Gson().toJson(tracks)
        sharedPreferences.edit()
            .putString(SEARCH_HISTORY_KEY, json)
            .apply()
    }
}