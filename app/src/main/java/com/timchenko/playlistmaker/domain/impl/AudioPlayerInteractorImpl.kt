package com.timchenko.playlistmaker.domain.impl

import com.timchenko.playlistmaker.domain.AudioPlayerInteractor
import com.timchenko.playlistmaker.domain.models.State
import com.timchenko.playlistmaker.domain.repository.MediaPlayerRepository

class AudioPlayerInteractorImpl(
    private val medialPlayerRepository : MediaPlayerRepository
    ) : AudioPlayerInteractor {

    var state = State.DEFAULT

    override fun startPlayer() {
        medialPlayerRepository.startPlayer()
        state = State.PLAYING
    }

    override fun pausePlayer() {
        medialPlayerRepository.pausePlayer()
        state = State.PAUSED
    }

    override fun preparePlayer(url: String?, onCompletePlaying: () -> Unit) {
        if (state == State.DEFAULT) {
            medialPlayerRepository.preparePlayer(url)
            medialPlayerRepository.setListenersPlayer(
                { state = State.PREPARED },
                {
                    state = State.PREPARED
                    onCompletePlaying()
                })
        }
    }

    override fun releasePlayer() {
        medialPlayerRepository.releasePlayer()
        state = State.DEFAULT
    }

    override fun getCurrentState() = state

    override fun getCurrentPosition(): Int = medialPlayerRepository.getCurrentPositionPlayer()
}