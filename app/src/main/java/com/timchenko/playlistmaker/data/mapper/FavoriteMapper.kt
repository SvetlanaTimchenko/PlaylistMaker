package com.timchenko.playlistmaker.data.mapper

import com.timchenko.playlistmaker.data.db.entity.FavoriteEntity
import com.timchenko.playlistmaker.data.dto.TrackDto
import com.timchenko.playlistmaker.domain.models.Track

class FavoriteMapper {
    fun map(track: TrackDto): FavoriteEntity {
        return FavoriteEntity(
            trackId = track.trackId,
            trackName = track.trackName,
        )
    }

    fun map(track: FavoriteEntity): Track {
        return Track(
            trackId = track.trackId,
            trackName = track.trackName,
        )
    }

    fun map(track: Track): FavoriteEntity {
        return FavoriteEntity(
            trackId = track.trackId,
            trackName = track.trackName,
        )
    }
}