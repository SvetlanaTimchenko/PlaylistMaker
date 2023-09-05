package com.timchenko.playlistmaker.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timchenko.playlistmaker.domain.SearchHistoryInteractor
import com.timchenko.playlistmaker.domain.TracksInteractor
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.ui.search.TracksState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor,
    private val searchHistoryInteractor: SearchHistoryInteractor
): ViewModel() {

    private var searchJob: Job? = null
    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private val historyLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeHistoryState(): LiveData<ArrayList<Track>> = historyLiveData

    override fun onCleared() {
        searchJob?.cancel()
    }

    fun searchDebounce(changedText: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            makeSearch(changedText)
        }
    }

    private fun makeSearch(text: String) {
        if (text.isNotEmpty()) {
            renderState(TracksState.Loading)

            viewModelScope.launch {
                tracksInteractor.searchTracks(text).collect { pair ->
                    processSearchResult(pair.first, pair.second)
                }
            }
        }
    }

    private fun processSearchResult(foundTracks: List<Track>?, errorMessage: Int?) {
        when {
            errorMessage != null -> {
                renderState(TracksState.Error)
            }

            foundTracks.isNullOrEmpty() -> {
                renderState(TracksState.Empty)
            }

            else -> {
                renderState(
                    TracksState.Content(
                        tracks = foundTracks
                    )
                )
            }
        }
    }

    fun getSearchHistory() {
        historyLiveData.postValue(searchHistoryInteractor.getFromHistory())
    }

    fun clearHistory() {
        searchHistoryInteractor.clearHistory()
    }

    fun onClick(track: Track) {
        searchHistoryInteractor.addTrack(track) // добавляем трек в историю поиска
    }

    private fun renderState(state: TracksState) {
        stateLiveData.postValue(state)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}