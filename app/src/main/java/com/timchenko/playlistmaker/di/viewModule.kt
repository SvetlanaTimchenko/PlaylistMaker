package com.timchenko.playlistmaker.di

import com.timchenko.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.timchenko.playlistmaker.presentation.main.MainViewModel
import com.timchenko.playlistmaker.presentation.media.FavoritesFragmentViewModel
import com.timchenko.playlistmaker.presentation.media.PlaylistsFragmentViewModel
import com.timchenko.playlistmaker.presentation.search.SearchViewModel
import com.timchenko.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        AudioPlayerViewModel(get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        MainViewModel(get())
    }

    viewModel {
        FavoritesFragmentViewModel()
    }

    viewModel {
        PlaylistsFragmentViewModel()
    }
}