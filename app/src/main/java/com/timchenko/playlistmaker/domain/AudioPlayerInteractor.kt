package com.timchenko.playlistmaker.domain

import com.timchenko.playlistmaker.domain.models.State

interface AudioPlayerInteractor {
    fun preparePlayer(url : String, onStateChanged : (s: State) -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun switchPlayer(onStateChangedTo: (s: State) -> Unit)
    fun shutDownPlayer()
}