package com.timchenko.playlistmaker.ui.search

import com.timchenko.playlistmaker.domain.models.Track

sealed interface TracksState {

    object Loading : TracksState

    object Error : TracksState

    object Empty : TracksState

    object Default: TracksState

    data class Content(
        val tracks: List<Track>
    ) : TracksState

}
