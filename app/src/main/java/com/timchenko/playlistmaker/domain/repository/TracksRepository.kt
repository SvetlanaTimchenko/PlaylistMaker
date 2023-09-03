package com.timchenko.playlistmaker.domain.repository

import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks(expression: String) : Flow<Resource<List<Track>>>
}