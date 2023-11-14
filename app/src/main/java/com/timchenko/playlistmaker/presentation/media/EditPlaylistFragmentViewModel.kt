package com.timchenko.playlistmaker.presentation.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.timchenko.playlistmaker.domain.PlaylistInteractor
import com.timchenko.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.launch

class EditPlaylistFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor
): AddPlaylistFragmentViewModel(playlistInteractor) {
    private val playlistData = MutableLiveData<Playlist>()
    fun observePlaylist(): LiveData<Playlist> = playlistData
    fun getPlaylist(id: Int) {
        viewModelScope.launch {
            playlistInteractor.getById(id).collect {
                playlistData.postValue(it)
                super.playlist = it
            }
        }
    }
    suspend fun savePlaylist(playlist: Playlist) {
        viewModelScope.launch {
            playlistInteractor.update(playlist)
        }
    }
}