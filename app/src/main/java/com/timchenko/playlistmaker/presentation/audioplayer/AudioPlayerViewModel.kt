package com.timchenko.playlistmaker.presentation.audioplayer

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timchenko.playlistmaker.domain.AudioPlayerInteractor
import com.timchenko.playlistmaker.domain.models.State
import com.timchenko.playlistmaker.ui.audioplayer.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(
    private val audioPlayerInteractor: AudioPlayerInteractor
) : ViewModel() {

    private val handler = Handler(Looper.getMainLooper())

    private val stateLiveData = MutableLiveData<PlayerState>()
    fun observeStateLiveData():LiveData<PlayerState> = stateLiveData

    private val timeLiveData = MutableLiveData<String>()
    fun observeTimeLiveData(): LiveData<String> = timeLiveData

    private val playerRunnable = object : Runnable {
        override fun run() {
            if (audioPlayerInteractor.getCurrentState() == State.PLAYING ) {
                renderState(PlayerState.PLAYING)
                timeLiveData.postValue(getDateFormat())
                handler.postDelayed(this, DELAY_UPDATE_TIME_MS)
            }
        }
    }

    fun preparePlayer (previewUrl: String){
        audioPlayerInteractor.preparePlayer(previewUrl) {
            renderState(PlayerState.PREPARED)
        }
        renderState(PlayerState.PREPARED)
    }

    private fun startPlayer(){
        audioPlayerInteractor.startPlayer()
        renderState(PlayerState.PLAYING)
        updateTime()
    }

    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        renderState(PlayerState.PAUSED)
        handler.removeCallbacks(playerRunnable)
    }

    fun playbackControl(){
        if(audioPlayerInteractor.getCurrentState() == State.PLAYING) {
            pausePlayer()
        }
        else{
            handler.removeCallbacks(playerRunnable)
            startPlayer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        audioPlayerInteractor.releasePlayer()
        handler.removeCallbacks(playerRunnable)
    }

     private fun getDateFormat(): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault())
            .format(audioPlayerInteractor.getCurrentPosition())
    }

    private fun updateTime() {
        handler.postDelayed(playerRunnable, DELAY_UPDATE_TIME_MS)
    }

    fun renderState(state: PlayerState) {
        stateLiveData.postValue(state)
    }

    companion object{
        private const val DELAY_UPDATE_TIME_MS = 200L
    }
}