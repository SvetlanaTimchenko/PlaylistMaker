package com.timchenko.playlistmaker.data.repository

import com.timchenko.playlistmaker.data.NetworkClient
import com.timchenko.playlistmaker.data.db.AppDatabase
import com.timchenko.playlistmaker.data.mapper.TrackMapper
import com.timchenko.playlistmaker.data.dto.ITunesResponse
import com.timchenko.playlistmaker.data.dto.TrackSearchRequest
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.domain.repository.TracksRepository
import com.timchenko.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase
    ) : TracksRepository {
    override fun searchTracks(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                val tracks = (response as ITunesResponse).results.map {
                    TrackMapper.map(trackDto = it)
                }
                val favorites = appDatabase.getFavoriteDao().getIds()
                if (favorites != null) {
                    setFavoritesToTracks(tracks, favorites)
                }
                emit(Resource.Success(tracks))
            }
            else -> {
                emit(Resource.Error(response.resultCode)) // "Ошибка сервера"
            }
        }
    }

    private fun setFavoritesToTracks(tracks: List<Track>, indicators: List<Int>) {
        for (i in tracks) {
            if (i.trackId in indicators) {
                i.isFavorite = true
            }
        }
    }
}