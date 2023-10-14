package com.timchenko.playlistmaker.presentation.models

sealed interface PlaylistTrackState {
    class Match(val playlistName: String? = null): PlaylistTrackState
    class Added(val playlistName: String? =  null): PlaylistTrackState
}