package com.timchenko.playlistmaker.domain

import com.timchenko.playlistmaker.domain.models.State

interface AudioPlayerInteractor {
    fun preparePlayer(url:String?, onCompletePlaying:() -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentState(): State
    fun getCurrentPosition(): Int
}