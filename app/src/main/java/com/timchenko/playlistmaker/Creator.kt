package com.timchenko.playlistmaker

import com.timchenko.playlistmaker.data.MediaPlayerRepositoryImpl
import com.timchenko.playlistmaker.domain.AudioPlayerInteractor
import com.timchenko.playlistmaker.domain.impl.AudioPlayerInteractorImpl

object Creator {
    fun provideAudioPlayerInteractor() : AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(MediaPlayerRepositoryImpl())
    }
}