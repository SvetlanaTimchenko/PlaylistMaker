package com.timchenko.playlistmaker.di

import com.timchenko.playlistmaker.domain.AudioPlayerInteractor
import com.timchenko.playlistmaker.domain.FavoriteInteractor
import com.timchenko.playlistmaker.domain.SearchHistoryInteractor
import com.timchenko.playlistmaker.domain.SettingsInteractor
import com.timchenko.playlistmaker.domain.SharingInteractor
import com.timchenko.playlistmaker.domain.TracksInteractor
import com.timchenko.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.timchenko.playlistmaker.domain.impl.FavoriteInteractorImpl
import com.timchenko.playlistmaker.domain.impl.SearchHistoryInteractorImpl
import com.timchenko.playlistmaker.domain.impl.SettingsInteractorImpl
import com.timchenko.playlistmaker.domain.impl.SharingInteractorImpl
import com.timchenko.playlistmaker.domain.impl.TracksInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<SearchHistoryInteractor> {
        SearchHistoryInteractorImpl(get())
    }

    factory<AudioPlayerInteractor> {
        AudioPlayerInteractorImpl(get())
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    single<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }
}