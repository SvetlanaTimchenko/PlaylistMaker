package com.timchenko.playlistmaker.data.repository

import com.timchenko.playlistmaker.data.NetworkClient
import com.timchenko.playlistmaker.data.mapper.TrackMapper
import com.timchenko.playlistmaker.data.dto.ITunesResponse
import com.timchenko.playlistmaker.data.dto.TrackSearchRequest
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.domain.repository.TracksRepository
import com.timchenko.playlistmaker.util.Resource

class TracksRepositoryImpl(private val networkClient: NetworkClient) : TracksRepository {
    override fun searchTracks(expression: String): Resource<List<Track>> {
        val response = networkClient.doRequest(TrackSearchRequest(expression))
        return when (response.resultCode) {
            200 -> {
                Resource.Success((response as ITunesResponse).results.map { trackDto ->
                    TrackMapper.map(trackDto = trackDto)
                })
            }
            else -> {
                Resource.Error(response.resultCode) // "Ошибка сервера"
            }
        }
    }
}