package com.timchenko.playlistmaker.data.mapper

import com.timchenko.playlistmaker.data.dto.TrackDto
import com.timchenko.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

object TrackMapper {
    fun map(trackDto: TrackDto): Track {
        return Track(
            trackId = trackDto.trackId,
            trackName = trackDto.trackName,
            artistName = trackDto.artistName,
            trackTime = convertMillisToString(millis = trackDto.trackTimeMillis),
            artworkUrl100 = trackDto.artworkUrl100,
            collectionName = trackDto.collectionName,
            releaseDate = trackDto.releaseDate,
            primaryGenreName = trackDto.primaryGenreName,
            country = trackDto.country,
            previewUrl = trackDto.previewUrl
        )
    }

    private fun convertMillisToString(millis: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
    }
}