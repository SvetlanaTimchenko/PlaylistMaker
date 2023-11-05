package com.timchenko.playlistmaker.domain

import com.timchenko.playlistmaker.domain.models.Playlist
import com.timchenko.playlistmaker.domain.models.PlaylistTracks
import com.timchenko.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getAll(): Flow<List<Playlist>>
    suspend fun add(playlist: Playlist)
    suspend fun update(playlist: Playlist)
    suspend fun delete(playlistId: Int)
    suspend fun getById(id: Int): Flow<Playlist>
    suspend fun addPlaylistTracks(playlistTracks: PlaylistTracks)
    suspend fun deletePlaylistTrack(playlistId: Int, trackId: Int)
    suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>>
}