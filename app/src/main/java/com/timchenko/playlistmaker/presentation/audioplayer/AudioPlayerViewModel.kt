package com.timchenko.playlistmaker.presentation.audioplayer

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.timchenko.playlistmaker.domain.AudioPlayerInteractor
import com.timchenko.playlistmaker.domain.models.State
import com.timchenko.playlistmaker.util.Creator

class AudioPlayerViewModel : ViewModel() {

    private val audioPlayerInteractor : AudioPlayerInteractor = Creator.provideAudioPlayerInteractor()

    private val handler = Handler(Looper.getMainLooper())
    private lateinit var playerRunnable: Runnable

    private val playButtonState = MutableLiveData<Boolean>()
    fun observePlayButtonState() : LiveData<Boolean> = playButtonState

    private val playButtonEnableState = MutableLiveData<Boolean>()
    fun observePlayButtonEnabledState() : LiveData<Boolean> = playButtonEnableState

    private val secondsState = MutableLiveData<Long>()
    fun observeSecondsState(): LiveData<Long> = secondsState

    override fun onCleared() {
        audioPlayerInteractor.shutDownPlayer()
        handler.removeCallbacksAndMessages(null)
    }

    fun preparePlayer(previewUrl: String) {
        audioPlayerInteractor.preparePlayer(url = previewUrl) { state ->
            when (state) {
                State.PREPARED -> {
                    playButtonEnableState.postValue(true)
                }
                else -> {}
            }
        }
    }

    fun pausePlayer() {
        audioPlayerInteractor.pausePlayer()
        playButtonState.postValue(false)
        handler.removeCallbacksAndMessages(null)
    }

    fun playbackControl(secondsCount: Long) {
        if (secondsCount > 0) {
            audioPlayerInteractor.switchPlayer { state ->
                when (state) {
                    State.PAUSED, State.PREPARED -> {
                        playButtonState.postValue(false)
                        handler.removeCallbacks(playerRunnable)
                    }
                    State.PLAYING -> {
                        playButtonState.postValue(true)
                        startTimer(secondsCount)
                    }
                    else -> {}
                }
            }
        }
    }



    private fun startTimer(duration: Long) {
        val startTime = System.currentTimeMillis()
        playerRunnable = createUpdateTimerTask(startTime, duration * DELAY_MS)
        handler.post(playerRunnable)
    }

    private fun createUpdateTimerTask(startTime: Long, duration: Long): Runnable {
        return object : Runnable {
            override fun run() {
                // Сколько прошло времени с момента запуска таймера
                val elapsedTime = System.currentTimeMillis() - startTime
                // Сколько осталось до конца
                val remainingTime = duration - elapsedTime

                if (remainingTime > 0) {
                    // Если всё ещё отсчитываем секунды —
                    // обновляем UI и снова планируем задачу
                    secondsState.postValue(remainingTime / DELAY_MS)
                    handler.postDelayed(this, DELAY_MS)
                } else {
                    pausePlayer()
                }
            }
        }
    }

    companion object {
        private const val DELAY_MS = 1000L
    }
}