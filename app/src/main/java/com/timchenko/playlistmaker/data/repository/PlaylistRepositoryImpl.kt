package com.timchenko.playlistmaker.data.repository

import com.timchenko.playlistmaker.data.ImageStorage
import com.timchenko.playlistmaker.data.db.AppDatabase
import com.timchenko.playlistmaker.data.db.entity.PlaylistEntity
import com.timchenko.playlistmaker.data.mapper.PlaylistMapper
import com.timchenko.playlistmaker.domain.models.Playlist
import com.timchenko.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistMapper: PlaylistMapper,
    private val storage: ImageStorage
) : PlaylistRepository {
    override suspend fun getAll(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.gerPlaylistDao().getAll()
        playlists?.let { convertFromPlaylistEntity(it) }?.let { emit(it) }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>) : List<Playlist> {
        return playlists.map { playlistMapper.map(it) }
    }

    override suspend fun add(playlist: Playlist) {
        // сохраняем картинку в хранилище приложения
        if (playlist.uri != null && playlist.uri != "") {
            playlist.uri = storage.saveImageInPrivateStorage(playlist.uri!!)
        }
        appDatabase.gerPlaylistDao().add(playlistMapper.map(playlist))
    }

    override suspend fun update(playlist: Playlist) {
        appDatabase.gerPlaylistDao().update(playlistMapper.map(playlist))
    }
}