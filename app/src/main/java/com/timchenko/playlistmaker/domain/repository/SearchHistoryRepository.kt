package com.timchenko.playlistmaker.domain.repository

import com.timchenko.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    suspend fun getFromHistory(): ArrayList<Track>
    fun clearHistory()
}