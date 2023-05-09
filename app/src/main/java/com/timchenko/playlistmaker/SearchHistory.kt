package com.timchenko.playlistmaker

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val SEARCH_HISTORY = ""

class SearchHistory(sharedPrefs: SharedPreferences) {

    private val localSharedPrefs = sharedPrefs
    private var tracks = ArrayList<Track>()
    private val editor: Editor = localSharedPrefs.edit()

    fun addTrack(track: Track) {
        tracks = getFromHistory()
        if (tracks.size < 10) {
            if (tracks.contains(track)) {
                tracks.remove(track)
            }
        }
        else {
            tracks.removeLast()
        }
        tracks.add(0, track)
        saveToHistory(tracks)
    }

    private fun saveToHistory(tracks: ArrayList<Track>) {
        editor.putString(SEARCH_HISTORY, Gson().toJson(tracks)).commit()
    }

    fun getFromHistory(): ArrayList<Track> {
        val stringTracks = localSharedPrefs.getString(SEARCH_HISTORY, null)

        if (stringTracks != null) {
            return Gson().fromJson<ArrayList<Track>>(stringTracks, object :
                TypeToken<ArrayList<Track>>() {}.type)
        }
        return ArrayList<Track>()
    }

    fun clearHistory() {
        editor.remove(SEARCH_HISTORY).commit()
    }

}