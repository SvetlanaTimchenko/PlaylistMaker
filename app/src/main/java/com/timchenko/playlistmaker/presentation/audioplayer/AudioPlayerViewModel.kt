package com.timchenko.playlistmaker.presentation.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timchenko.playlistmaker.domain.AudioPlayerInteractor
import com.timchenko.playlistmaker.domain.FavoriteInteractor
import com.timchenko.playlistmaker.domain.PlaylistInteractor
import com.timchenko.playlistmaker.domain.models.Playlist
import com.timchenko.playlistmaker.domain.models.PlaylistTracks
import com.timchenko.playlistmaker.domain.models.State
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.presentation.models.PlaylistState
import com.timchenko.playlistmaker.presentation.models.PlaylistTrackState
import com.timchenko.playlistmaker.ui.audioplayer.PlayerState
import com.timchenko.playlistmaker.util.Formatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState

    private var isFavourite = MutableLiveData<Boolean>()
    fun observeFavoriteState(): LiveData<Boolean> = isFavourite

    private val playlistState = MutableLiveData<PlaylistState>(PlaylistState.Empty)
    fun observePlaylistState(): LiveData<PlaylistState> = playlistState

    private val addedToPlaylistState = MutableLiveData<PlaylistTrackState>()
    fun observeAddedToPlaylistState(): LiveData<PlaylistTrackState> = addedToPlaylistState

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        releasePlayer()
    }

    fun onPause() {
        pausePlayer()
    }

    fun playbackControl() {
        if(audioPlayerInteractor.getCurrentState() == State.PLAYING) {
            pausePlayer()
        }
        else{
            startPlayer()
        }
    }

    fun preparePlayer(previewUrl: String?) {
        audioPlayerInteractor.preparePlayer(previewUrl) {
            playerState.postValue(PlayerState.Prepared())
            timerJob?.cancel()
        }
        playerState.postValue(PlayerState.Prepared())
    }

    private fun startPlayer() {
        audioPlayerInteractor.startPlayer()
        playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
        startTimer()
    }

    private fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        playerState.postValue(PlayerState.Paused(getCurrentPlayerPosition()))
        timerJob?.cancel()
    }

    private fun releasePlayer() {
        audioPlayerInteractor.releasePlayer()
        playerState.value = PlayerState.Default()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (audioPlayerInteractor.getCurrentState() == State.PLAYING) {
                delay(DELAY_UPDATE_TIME_MS)
                playerState.postValue(PlayerState.Playing(getCurrentPlayerPosition()))
            }
        }
    }

    private fun getCurrentPlayerPosition(): String {
//        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(audioPlayerInteractor.getCurrentPosition()) ?: "00:00"
        return Formatter.convertMillisToMinutesAndSeconds(audioPlayerInteractor.getCurrentPosition()) ?: "00:00"
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            if (track.isFavorite) {
                favoriteInteractor.delete(track)
            } else {
                favoriteInteractor.add(track)
            }
            isFavourite.postValue(!track.isFavorite)
        }
    }

    fun addTrackInPlaylist(playlist: Playlist, track: Track) {
        if (playlist.tracks.isEmpty()) {
            addTrackToPlaylist(playlist, track)
            addedToPlaylistState.postValue(PlaylistTrackState.Added(playlist.name))
        }
        else {
            if (playlist.tracks.contains(track.trackId)) {
                addedToPlaylistState.postValue(PlaylistTrackState.Match(playlist.name))
            }
            else {
                addTrackToPlaylist(playlist, track)
                addedToPlaylistState.postValue(PlaylistTrackState.Added(playlist.name))
            }
        }
    }

    private fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        playlist.tracks.add(track.trackId)
        playlist.tracksCounter += 1
        playlist.trackTimerMillis += track.trackTimeMillis ?: 0

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                playlistInteractor.update(playlist)
            }
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val playlistTrack = PlaylistTracks(
                    id = null,
                    playlistId = playlist.id!!,
                    trackId = track.trackId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackTimeMillis = track.trackTimeMillis,
                    artworkUrl100 = track.artworkUrl100,
                    collectionName = track.collectionName,
                    releaseDate = track.releaseDate,
                    primaryGenreName = track.primaryGenreName,
                    country = track.country,
                    previewUrl = track.previewUrl
                )
                playlistInteractor.addPlaylistTracks(playlistTrack)
            }
        }
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

    companion object{
        private const val DELAY_UPDATE_TIME_MS = 300L
    }
}