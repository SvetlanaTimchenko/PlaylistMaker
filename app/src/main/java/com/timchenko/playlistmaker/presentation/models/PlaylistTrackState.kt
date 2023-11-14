package com.timchenko.playlistmaker.presentation.models

import com.timchenko.playlistmaker.domain.models.Track

sealed interface PlaylistTrackState {
    class Match(val playlistName: String? = null): PlaylistTrackState
    class Added(val playlistName: String? =  null): PlaylistTrackState
    class Content(val tracks: List<Track>): PlaylistTrackState
    object Empty: PlaylistTrackState
}