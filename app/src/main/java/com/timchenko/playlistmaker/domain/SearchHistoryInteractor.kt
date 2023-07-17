package com.timchenko.playlistmaker.domain

import com.timchenko.playlistmaker.domain.models.Track

interface SearchHistoryInteractor {
    fun addTrack(track: Track)
    fun getFromHistory(): ArrayList<Track>
    fun clearHistory()
}