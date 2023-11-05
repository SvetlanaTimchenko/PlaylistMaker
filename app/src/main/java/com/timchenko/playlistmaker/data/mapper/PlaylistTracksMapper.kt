package com.timchenko.playlistmaker.data.mapper

import com.timchenko.playlistmaker.data.db.entity.PlaylistTracksEntity
import com.timchenko.playlistmaker.domain.models.PlaylistTracks

class PlaylistTracksMapper {
    fun map(playlistTracks: PlaylistTracks): PlaylistTracksEntity {
        return PlaylistTracksEntity(
            id = playlistTracks.id,
            playlistId = playlistTracks.playlistId,
            trackId = playlistTracks.trackId,
            trackName = playlistTracks.trackName,
            artistName = playlistTracks.artistName,
            trackTimeMillis = playlistTracks.trackTimeMillis,
            artworkUrl100 = playlistTracks.artworkUrl100,
            collectionName = playlistTracks.collectionName,
            releaseDate = playlistTracks.releaseDate,
            primaryGenreName = playlistTracks.primaryGenreName,
            country = playlistTracks.country,
            previewUrl = playlistTracks.previewUrl
        )
    }
    fun map(playlistTracksEntity: PlaylistTracksEntity): PlaylistTracks {
        return PlaylistTracks(
            id = playlistTracksEntity.id,
            playlistId = playlistTracksEntity.playlistId,
            trackId = playlistTracksEntity.trackId,
            trackName = playlistTracksEntity.trackName,
            artistName = playlistTracksEntity.artistName,
            trackTimeMillis = playlistTracksEntity.trackTimeMillis,
            artworkUrl100 = playlistTracksEntity.artworkUrl100,
            collectionName = playlistTracksEntity.collectionName,
            releaseDate = playlistTracksEntity.releaseDate,
            primaryGenreName = playlistTracksEntity.primaryGenreName,
            country = playlistTracksEntity.country,
            previewUrl = playlistTracksEntity.previewUrl
        )
    }
}