package com.timchenko.playlistmaker.domain

import com.timchenko.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrack(track: Track)
    suspend fun getFromHistory(): ArrayList<Track>
    fun clearHistory()
}