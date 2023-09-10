package com.example.playlistmakerprod

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.util.Date

data class Track(val trackId:Int, val trackName:String?,
                 val artistName: String?,
                 @SerializedName("trackTimeMillis") val trackTime: String?,
                 val artworkUrl100: String?,
                 val country:String?,
                 val primaryGenreName:String?,
                 val collectionName:String?,
                 val previewUrl:String?,
                 val releaseDate:String?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }
    override fun describeContents(): Int {
        return 0
    }
    override fun writeToParcel(parcel: Parcel, p1: Int) {
        parcel.writeInt(trackId)
        parcel.writeString(trackName)
        parcel.writeString(artistName)
        parcel.writeString(trackTime)
        parcel.writeString(artworkUrl100)
        parcel.writeString(country)
        parcel.writeString(primaryGenreName)
        parcel.writeString(collectionName)
        parcel.writeString(previewUrl)
        parcel.writeString(releaseDate)
    }
    companion object CREATOR : Parcelable.Creator<Track> {
        override fun createFromParcel(parcel: Parcel): Track {
            return Track(parcel)
        }

        override fun newArray(size: Int): Array<Track?> {
            return arrayOfNulls(size)
        }
    }
}
