package com.timchenko.playlistmaker

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.timchenko.playlistmaker.domain.models.Track

class SearchHistory(sharedPrefs: SharedPreferences) {

    companion object {
        const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY"
    }

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
        editor.putString(SEARCH_HISTORY_KEY, Gson().toJson(tracks)).commit()
    }

    fun getFromHistory(): ArrayList<Track> {
        val stringTracks = localSharedPrefs.getString(SEARCH_HISTORY_KEY, null)

        if (stringTracks != null) {
            return Gson().fromJson<ArrayList<Track>>(stringTracks, object :
                TypeToken<ArrayList<Track>>() {}.type)
        }
        return ArrayList<Track>()
    }

    fun clearHistory() {
        editor.remove(SEARCH_HISTORY_KEY).commit()
    }

}