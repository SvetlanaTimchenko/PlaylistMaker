package com.timchenko.playlistmaker.domain.repository

import com.timchenko.playlistmaker.domain.models.Track

interface SearchHistoryRepository {
    fun addTrack(track: Track)
    fun getFromHistory(): ArrayList<Track>
    fun clearHistory()
}