package com.timchenko.playlistmaker.data.repository

import android.media.MediaPlayer
import com.timchenko.playlistmaker.domain.repository.MediaPlayerRepository
import com.timchenko.playlistmaker.domain.models.State

class MediaPlayerRepositoryImpl : MediaPlayerRepository {

    private var playerState = State.DEFAULT
    private var mediaPlayer = MediaPlayer()

    override fun preparePlayer(url : String, onStateChanged : (s: State) -> Unit) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = State.PREPARED
            onStateChanged(State.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            playerState = State.PREPARED
            onStateChanged(State.PREPARED)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = State.PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = State.PAUSED
    }

    override fun switchPlayerState(onStateChangedTo: (s: State) -> Unit) {
        when (playerState) {
            State.DEFAULT -> {}
            State.PLAYING -> {
                mediaPlayer.pause()
                playerState = State.PAUSED
                onStateChangedTo(State.PAUSED)
            }
            State.PREPARED, State.PAUSED -> {
                mediaPlayer.start()
                playerState = State.PLAYING
                onStateChangedTo(State.PLAYING)
            }
        }
    }

    override fun shutDownPlayer() {
        mediaPlayer.release()
    }
}