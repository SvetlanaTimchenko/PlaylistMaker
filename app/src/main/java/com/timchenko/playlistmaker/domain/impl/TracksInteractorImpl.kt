package com.timchenko.playlistmaker.domain.impl

import com.timchenko.playlistmaker.domain.TracksInteractor
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.domain.repository.TracksRepository
import com.timchenko.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, Int?>> {
        return repository.searchTracks(expression).map { result ->
            when (result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }
}