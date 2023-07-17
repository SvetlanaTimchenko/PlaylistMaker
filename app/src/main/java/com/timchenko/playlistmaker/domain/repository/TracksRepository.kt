package com.timchenko.playlistmaker.domain.repository

import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.util.Resource

interface TracksRepository {
    fun searchTracks(expression: String) : Resource<List<Track>>
}