package com.timchenko.playlistmaker.presentation.search

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.timchenko.playlistmaker.domain.TracksInteractor
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.ui.search.TracksState
import com.timchenko.playlistmaker.util.Creator

class SearchViewModel(application: Application): AndroidViewModel(application) {

    private val tracksInteractor by lazy { Creator.provideTracksInteractor(context = getApplication<Application>()) }
    private val searchHistoryInteractor by lazy { Creator.provideSearchHistoryInteractor(context = getApplication<Application>()) }
    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<TracksState>()
    fun observeState(): LiveData<TracksState> = stateLiveData

    private val historyLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeHistoryState(): LiveData<ArrayList<Track>> = historyLiveData

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce(changedText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { makeSearch(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime,
        )
    }

    private fun makeSearch(text: String) {
        if (text.isNotEmpty()) {
            renderState(TracksState.Loading)

            tracksInteractor.searchTracks(text, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: Int?) {
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
                                    tracks =  foundTracks
                                )
                            )
                        }
                    }
                }
            })
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
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SearchViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

}