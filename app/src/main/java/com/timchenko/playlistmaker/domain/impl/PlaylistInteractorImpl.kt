package com.timchenko.playlistmaker.domain.impl

import com.timchenko.playlistmaker.domain.PlaylistInteractor
import com.timchenko.playlistmaker.domain.models.Playlist
import com.timchenko.playlistmaker.domain.models.PlaylistTracks
import com.timchenko.playlistmaker.domain.models.Track
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

    override suspend fun getById(id: Int): Flow<Playlist> {
        return repository.getById(id)
    }

    override suspend fun delete(playlistId: Int) {
        repository.delete(playlistId)
    }
    override suspend fun addPlaylistTracks(playlistTracks: PlaylistTracks) {
        return repository.addPlaylistTracks(playlistTracks)
    }

    override suspend fun deletePlaylistTrack(playlistId: Int, trackId: Int) {
        return repository.deletePlaylistTrack(playlistId, trackId)
    }

    override suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> {
        return repository.getPlaylistTracks(playlistId)
    }
}