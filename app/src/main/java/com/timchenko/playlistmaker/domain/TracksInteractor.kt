package com.timchenko.playlistmaker.domain

import com.timchenko.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, Int?>>
}