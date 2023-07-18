package com.timchenko.playlistmaker.di

import com.timchenko.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.timchenko.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.timchenko.playlistmaker.data.repository.SettingsRepositoryImpl
import com.timchenko.playlistmaker.data.repository.TracksRepositoryImpl
import com.timchenko.playlistmaker.domain.repository.MediaPlayerRepository
import com.timchenko.playlistmaker.domain.repository.SearchHistoryRepository
import com.timchenko.playlistmaker.domain.repository.SettingsRepository
import com.timchenko.playlistmaker.domain.repository.TracksRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }
}