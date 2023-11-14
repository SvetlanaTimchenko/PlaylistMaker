package com.timchenko.playlistmaker.presentation.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timchenko.playlistmaker.domain.PlaylistInteractor
import com.timchenko.playlistmaker.domain.SharingInteractor
import com.timchenko.playlistmaker.domain.models.Playlist
import com.timchenko.playlistmaker.presentation.models.PlaylistTrackState
import kotlinx.coroutines.launch

class PlaylistDetailsFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {
    private val playlistData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistData

    private val tracksData = MutableLiveData<PlaylistTrackState>()
    fun observeTracks(): LiveData<PlaylistTrackState> = tracksData

    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor.getById(id).collect {
                playlistData.postValue(it)
            }
        }
    }

    fun getTracksForPlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor.getPlaylistTracks(id).collect {
                if (it.isEmpty()) {
                    renderState(PlaylistTrackState.Empty)
                }
                else {
                    renderState(PlaylistTrackState.Content(it))
                }
            }
        }
    }

    fun shareApp(message: String) {
        sharingInteractor.sharePlaylist(message)
    }

    fun deleteTrackFromPlaylist(playlistId: Int, trackId: Int) {
        viewModelScope.launch {
            playlistInteractor.deletePlaylistTrack(playlistId, trackId)
            getPlaylist(playlistId)
            getTracksForPlaylist(playlistId)
        }
    }

    fun deletePlaylist(playlistId: Int) {
        viewModelScope.launch {
            playlistInteractor.delete(playlistId)
        }
    }

    private fun renderState(state: PlaylistTrackState) {
        tracksData.postValue(state)
    }
}