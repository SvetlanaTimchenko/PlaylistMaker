package com.timchenko.playlistmaker.domain.repository

interface MediaPlayerRepository {
    fun preparePlayer(url: String?)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPositionPlayer(): Int
    fun setListenersPlayer(
        onPrepared: () -> Unit,
        onCompleteListener: () -> Unit
    )
}