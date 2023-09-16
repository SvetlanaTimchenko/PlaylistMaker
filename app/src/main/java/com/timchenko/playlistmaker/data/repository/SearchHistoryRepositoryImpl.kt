package com.timchenko.playlistmaker.data.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.timchenko.playlistmaker.data.db.AppDatabase
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryRepositoryImpl(
    private val localSharedPrefs: SharedPreferences,
    private val gson: Gson,
    private val appDatabase: AppDatabase
) : SearchHistoryRepository {

    private var tracks = ArrayList<Track>()
    private val editor: SharedPreferences.Editor = localSharedPrefs.edit()

    override fun addTrack(track: Track) {
        tracks = getHistoryData()
        if (tracks.size < MAX_NUMBER_OF_TRACKS_IN_HISTORY) {
            for (i in 0 until tracks.size) {
                if (tracks[i].trackId == track.trackId) {
                    tracks.removeAt(i)
                    break
                }
            }
        }
        else {
            tracks.removeLast()
        }
        tracks.add(0, track)
        saveToHistory(tracks)
    }

    private fun saveToHistory(tracks: ArrayList<Track>) {
        editor.putString(SEARCH_HISTORY_KEY, gson.toJson(tracks)).commit()
    }

    private fun getHistoryData(): ArrayList<Track> {
        val stringTracks = localSharedPrefs.getString(SEARCH_HISTORY_KEY, null)

        if (stringTracks != null) {
            return Gson().fromJson(stringTracks, object :
                TypeToken<ArrayList<Track>>() {}.type)
        }
        return ArrayList()
    }

    override suspend fun getFromHistory(): ArrayList<Track> {
        val history = getHistoryData()
        val favorites = appDatabase.getFavoriteDao().getIds()
        if (favorites != null) {
            setFavoritesToTracks(history, favorites)
        }
        return history
    }

    private fun setFavoritesToTracks(tracks: ArrayList<Track>, indicators: List<Int>) {
        for (i in tracks) {
            i.isFavorite = i.trackId in indicators
        }
    }

    override fun clearHistory() {
        editor.remove(SEARCH_HISTORY_KEY).commit()
    }

    companion object {
        const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY"
        const val MAX_NUMBER_OF_TRACKS_IN_HISTORY = 10
    }
}