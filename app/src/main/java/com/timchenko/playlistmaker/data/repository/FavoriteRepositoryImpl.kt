package com.timchenko.playlistmaker.data.repository

import com.timchenko.playlistmaker.data.db.AppDatabase
import com.timchenko.playlistmaker.data.db.entity.FavoriteEntity
import com.timchenko.playlistmaker.data.mapper.FavoriteMapper
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val favoriteMapper: FavoriteMapper
) : FavoriteRepository {
    override suspend fun getAll(): Flow<List<Track>> = flow {
        val tracks = appDatabase.getFavoriteDao().getAll()
        tracks?.let { convertFromFavoriteEntity(it) }?.let { emit(it) }
    }

    private fun convertFromFavoriteEntity(tracks: List<FavoriteEntity>): List<Track> {
        return tracks.map { track -> favoriteMapper.map(track) }
    }

    override suspend fun add(track: Track) {
        appDatabase.getFavoriteDao().add(favoriteMapper.map(track))
    }

    override suspend fun delete(track: Track) {
        appDatabase.getFavoriteDao().delete(favoriteMapper.map(track))
    }
}