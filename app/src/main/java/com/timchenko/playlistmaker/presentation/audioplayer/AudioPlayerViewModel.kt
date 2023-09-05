package com.timchenko.playlistmaker.presentation.audioplayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.timchenko.playlistmaker.domain.AudioPlayerInteractor
import com.timchenko.playlistmaker.domain.models.State
import com.timchenko.playlistmaker.ui.audioplayer.PlayerState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private var timerJob: Job? = null

    private val playerState = MutableLiveData<PlayerState>(PlayerState.Default())
    fun observePlayerState(): LiveData<PlayerState> = playerState

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
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(audioPlayerInteractor.getCurrentPosition()) ?: "00:00"
    }

    companion object{
        private const val DELAY_UPDATE_TIME_MS = 300L
    }
}