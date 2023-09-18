package com.timchenko.playlistmaker.presentation.models

import com.timchenko.playlistmaker.domain.models.Track

sealed interface FavoriteState {
    data class Content(val tracks: List<Track>) : FavoriteState

    object Empty: FavoriteState
}