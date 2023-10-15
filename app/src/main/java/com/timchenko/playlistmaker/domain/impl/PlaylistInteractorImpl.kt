package com.timchenko.playlistmaker.domain.impl

import com.timchenko.playlistmaker.domain.PlaylistInteractor
import com.timchenko.playlistmaker.domain.models.Playlist
import com.timchenko.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val repository: PlaylistRepository
) : PlaylistInteractor {
    override suspend fun getAll(): Flow<List<Playlist>> {
        return repository.getAll()
    }

    override suspend fun add(playlist: Playlist) {
        repository.add(playlist)
    }

    override suspend fun update(playlist: Playlist) {
        repository.update(playlist)
    }
}