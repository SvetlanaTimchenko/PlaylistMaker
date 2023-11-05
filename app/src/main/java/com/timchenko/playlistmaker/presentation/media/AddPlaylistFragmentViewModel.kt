package com.timchenko.playlistmaker.presentation.media

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timchenko.playlistmaker.domain.PlaylistInteractor
import com.timchenko.playlistmaker.domain.models.Playlist

open class AddPlaylistFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {
    open var playlist = Playlist(tracks = ArrayList())
    private val playlistState = MutableLiveData<Playlist>()
    fun observePlaylistState() : LiveData<Playlist> = playlistState

    init {
        renderState(playlist)
    }

    fun addName(text: String) {
        playlist.name = text
        renderState(playlist)
    }

    fun addDescription(text: String) {
        playlist.description = text
        renderState(playlist)
    }

    fun addImage(uri: String) {
        playlist.uri = uri
        renderState(playlist)
    }

    suspend fun savePlaylist() {
        playlistInteractor.add(playlist)
    }

    private fun renderState(playlist: Playlist) {
        playlistState.postValue(playlist)
    }
}