package com.timchenko.playlistmaker.domain.impl

import com.timchenko.playlistmaker.domain.SearchHistoryInteractor
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.domain.repository.SearchHistoryRepository

class SearchHistoryInteractorImpl(
    private val searchHistoryRepository: SearchHistoryRepository
) : SearchHistoryInteractor {

    override fun addTrack(track: Track) {
        searchHistoryRepository.addTrack(track = track)
    }

    override suspend fun getFromHistory(): ArrayList<Track> {
        return searchHistoryRepository.getFromHistory()
    }

    override fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }

}