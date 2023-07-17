package com.timchenko.playlistmaker.presentation.mapper

import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.presentation.models.TrackDetails
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date
import java.util.Locale

object TrackMapper {
    fun map(track: Track): TrackDetails {
        return TrackDetails(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseYear = convertToYear(track.releaseDate),
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }
    private fun convertToYear(releaseDate: String): String {
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(Date.from(Instant.parse(releaseDate)))
    }
}