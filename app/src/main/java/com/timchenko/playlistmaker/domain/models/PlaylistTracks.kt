package com.timchenko.playlistmaker.domain.models

data class PlaylistTracks(
    val id: Int?,
    val playlistId: Int,
    val trackId: Int,
    val trackName: String? = null, // Название композиции
    val artistName: String? = null, // Имя исполнителя
    val trackTimeMillis: Int? = 0, // Продолжительность в миллисекундах
    val artworkUrl100: String? = null, // Ссылка на изображение обложки
    val collectionName: String? = null, // Название альбома
    val releaseDate: String? = null, // Год выпуска трека
    val primaryGenreName: String? = null, // жанр
    val country: String? = null, // Страна исполнителя
    val previewUrl: String? = null, // отрывок трека
)