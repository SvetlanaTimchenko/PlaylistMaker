package com.timchenko.playlistmaker.domain.impl

import com.timchenko.playlistmaker.domain.FavoriteInteractor
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository
) : FavoriteInteractor {
    override suspend fun getAll(): Flow<List<Track>> {
        return favoriteRepository.getAll()
    }

    override suspend fun add(track: Track) {
        favoriteRepository.add(track)
    }

    override suspend fun delete(track: Track) {
        favoriteRepository.delete(track)
    }
}