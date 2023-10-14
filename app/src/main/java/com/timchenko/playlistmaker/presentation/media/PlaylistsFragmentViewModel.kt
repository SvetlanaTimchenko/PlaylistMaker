package com.timchenko.playlistmaker.presentation.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timchenko.playlistmaker.domain.PlaylistInteractor
import com.timchenko.playlistmaker.presentation.models.PlaylistState
import kotlinx.coroutines.launch

class PlaylistsFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor
): ViewModel() {
    private val playlistState = MutableLiveData<PlaylistState>(PlaylistState.Empty)
    fun observePlaylistState(): LiveData<PlaylistState> = playlistState

    init {
        getPlaylists()
    }

    fun getPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAll().collect {
                if (it.isEmpty()) {
                    renderState(PlaylistState.Empty)
                }
                else {
                    renderState(PlaylistState.Content(it))
                }
            }
        }
    }

    private fun renderState(state: PlaylistState) {
        playlistState.postValue(state)
    }
}