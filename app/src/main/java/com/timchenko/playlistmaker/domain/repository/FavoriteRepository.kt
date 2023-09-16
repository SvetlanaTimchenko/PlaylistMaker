package com.timchenko.playlistmaker.domain.repository

import com.timchenko.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun getAll(): Flow<List<Track>>
    suspend fun add(track: Track)
    suspend fun delete(track: Track)
}