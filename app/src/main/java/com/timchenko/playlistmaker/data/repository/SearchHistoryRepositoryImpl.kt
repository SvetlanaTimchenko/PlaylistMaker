package com.timchenko.playlistmaker.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(context: Context) : SearchHistoryRepository {
    companion object {
        const val SHARED_PREFS = "playlist_maker"
        const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY"
    }

    private var sharedPrefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
    private val localSharedPrefs = sharedPrefs
    private var tracks = ArrayList<Track>()
    private val editor: SharedPreferences.Editor = localSharedPrefs.edit()

    override fun addTrack(track: Track) {
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

    override fun getFromHistory(): ArrayList<Track> {
        val stringTracks = localSharedPrefs.getString(SEARCH_HISTORY_KEY, null)

        if (stringTracks != null) {
            return Gson().fromJson(stringTracks, object :
                TypeToken<ArrayList<Track>>() {}.type)
        }
        return ArrayList()
    }

    override fun clearHistory() {
        editor.remove(SEARCH_HISTORY_KEY).commit()
    }
}