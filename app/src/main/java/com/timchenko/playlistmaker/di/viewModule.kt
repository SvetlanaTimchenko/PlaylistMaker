package com.timchenko.playlistmaker.di

import com.timchenko.playlistmaker.presentation.audioplayer.AudioPlayerViewModel
import com.timchenko.playlistmaker.presentation.media.AddPlaylistFragmentViewModel
import com.timchenko.playlistmaker.presentation.media.EditPlaylistFragmentViewModel
import com.timchenko.playlistmaker.presentation.media.FavoritesFragmentViewModel
import com.timchenko.playlistmaker.presentation.media.PlaylistDetailsFragmentViewModel
import com.timchenko.playlistmaker.presentation.media.PlaylistsFragmentViewModel
import com.timchenko.playlistmaker.presentation.root.RootViewModel
import com.timchenko.playlistmaker.presentation.search.SearchViewModel
import com.timchenko.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        SearchViewModel(get(), get())
    }

    viewModel {
        AudioPlayerViewModel(get(), get(), get())
    }

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        RootViewModel(get())
    }

    viewModel {
        FavoritesFragmentViewModel(get())
    }

    viewModel {
        PlaylistsFragmentViewModel(get())
    }

    viewModel {
        AddPlaylistFragmentViewModel(get())
    }

    viewModel {
        EditPlaylistFragmentViewModel(get())
    }

    viewModel {
        PlaylistDetailsFragmentViewModel(get(), get())
    }
}