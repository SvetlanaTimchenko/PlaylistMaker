package com.timchenko.playlistmaker.di

import com.timchenko.playlistmaker.data.mapper.FavoriteMapper
import com.timchenko.playlistmaker.data.mapper.PlaylistMapper
import com.timchenko.playlistmaker.data.mapper.PlaylistTracksMapper
import com.timchenko.playlistmaker.data.mapper.TrackMapper
import com.timchenko.playlistmaker.data.repository.FavoriteRepositoryImpl
import com.timchenko.playlistmaker.data.repository.MediaPlayerRepositoryImpl
import com.timchenko.playlistmaker.data.repository.PlaylistRepositoryImpl
import com.timchenko.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.timchenko.playlistmaker.data.repository.SettingsRepositoryImpl
import com.timchenko.playlistmaker.data.repository.TracksRepositoryImpl
import com.timchenko.playlistmaker.domain.repository.FavoriteRepository
import com.timchenko.playlistmaker.domain.repository.MediaPlayerRepository
import com.timchenko.playlistmaker.domain.repository.PlaylistRepository
import com.timchenko.playlistmaker.domain.repository.SearchHistoryRepository
import com.timchenko.playlistmaker.domain.repository.SettingsRepository
import com.timchenko.playlistmaker.domain.repository.TracksRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TracksRepository> {
        TracksRepositoryImpl(get(), get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get(), get())
    }

    factory<MediaPlayerRepository> {
        MediaPlayerRepositoryImpl(get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    factory { FavoriteMapper() }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    factory { PlaylistMapper() }
    factory { PlaylistTracksMapper() }
    factory { TrackMapper }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get(), get(), get(), get())
    }
}