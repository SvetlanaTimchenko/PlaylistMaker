package com.timchenko.playlistmaker.domain.impl

import com.timchenko.playlistmaker.domain.AudioPlayerInteractor
import com.timchenko.playlistmaker.domain.MediaPlayerRepository
import com.timchenko.playlistmaker.domain.models.State

class AudioPlayerInteractorImpl(
    private val medialPlayerRepository : MediaPlayerRepository
    ) : AudioPlayerInteractor {
    override fun preparePlayer(url: String, onStateChanged : (s: State) -> Unit) {
        medialPlayerRepository.preparePlayer(url, onStateChanged)
    }

    override fun startPlayer() {
        medialPlayerRepository.startPlayer()
    }

    override fun pausePlayer() {
        medialPlayerRepository.pausePlayer()
    }

    override fun switchPlayer(onStateChangedTo: (s: State) -> Unit) {
        medialPlayerRepository.switchPlayerState(onStateChangedTo)
    }

    override fun shutDownPlayer() {
        medialPlayerRepository.shutDownPlayer()
    }
}