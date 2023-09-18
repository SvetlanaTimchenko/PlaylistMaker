package com.timchenko.playlistmaker.domain

import com.timchenko.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {
    suspend fun getAll(): Flow<List<Track>>

    suspend fun add(track: Track)

    suspend fun delete(track: Track)

}