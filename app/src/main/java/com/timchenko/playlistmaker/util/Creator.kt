package com.timchenko.playlistmaker.util

import android.content.Context
import com.timchenko.playlistmaker.data.ExternalNavigator
import com.timchenko.playlistmaker.data.impl.ExternalNavigatorImpl
import com.timchenko.playlistmaker.data.repository.TracksRepositoryImpl
import com.timchenko.playlistmaker.data.network.RetrofitNetworkClient
import com.timchenko.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.timchenko.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.timchenko.playlistmaker.data.repository.SettingsRepositoryImpl
import com.timchenko.playlistmaker.domain.AudioPlayerInteractor
import com.timchenko.playlistmaker.domain.SearchHistoryInteractor
import com.timchenko.playlistmaker.domain.SettingsInteractor
import com.timchenko.playlistmaker.domain.SharingInteractor
import com.timchenko.playlistmaker.domain.TracksInteractor
import com.timchenko.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.timchenko.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.timchenko.playlistmaker.domain.impl.SettingsInteractorImpl
import com.timchenko.playlistmaker.domain.impl.SharingInteractorImpl
import com.timchenko.playlistmaker.domain.impl.TracksInteractorImpl
import com.timchenko.playlistmaker.domain.repository.SearchHistoryRepository
import com.timchenko.playlistmaker.domain.repository.SettingsRepository
import com.timchenko.playlistmaker.domain.repository.TracksRepository

object Creator {
    fun provideAudioPlayerInteractor() : AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(MediaPlayerRepositoryImpl())
    }

    fun provideSearchHistoryInteractor(context: Context): SearchHistoryInteractor {
        return SearchHistoryInteractorImpl(provideSearchHistoryRepository(context))
    }

    private fun provideSearchHistoryRepository(context: Context): SearchHistoryRepository {
        return SearchHistoryRepositoryImpl(context = context)
    }

    fun provideSettingsInteractor(context: Context) : SettingsInteractor {
        return SettingsInteractorImpl(settingsRepository = provideSettingsRepository(context = context))
    }

    private fun provideSettingsRepository(context: Context) : SettingsRepository {
        return SettingsRepositoryImpl(context = context)
    }

    fun provideSharingInteractor(context: Context) : SharingInteractor {
        return SharingInteractorImpl(externalNavigator = provideExternalNavigator(context))
    }

    private fun provideExternalNavigator(context: Context) : ExternalNavigator {
        return ExternalNavigatorImpl(context = context)
    }

    private fun getTracksRepository(context: Context): TracksRepository {
        return TracksRepositoryImpl(RetrofitNetworkClient(context = context))
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context = context))
    }
}