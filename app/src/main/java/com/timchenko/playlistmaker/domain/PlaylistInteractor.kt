package com.timchenko.playlistmaker.domain

import com.timchenko.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getAll(): Flow<List<Playlist>>
    suspend fun add(playlist: Playlist)
    suspend fun update(playlist: Playlist)
}