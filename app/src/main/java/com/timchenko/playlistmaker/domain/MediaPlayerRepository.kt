package com.timchenko.playlistmaker.domain

import com.timchenko.playlistmaker.domain.models.State

interface MediaPlayerRepository {
    fun preparePlayer(url : String, onStateChanged : (s: State) -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun switchPlayerState(onStateChangedTo: (s: State) -> Unit)
    fun shutDownPlayer()
}