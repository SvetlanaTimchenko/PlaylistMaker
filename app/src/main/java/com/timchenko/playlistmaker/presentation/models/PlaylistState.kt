package com.timchenko.playlistmaker.presentation.models

import com.timchenko.playlistmaker.domain.models.Playlist

sealed interface PlaylistState {
    data class Content(val playlists: List<Playlist>) : PlaylistState
    object Empty: PlaylistState
}
