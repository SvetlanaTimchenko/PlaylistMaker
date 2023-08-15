package com.timchenko.playlistmaker.data.dto

import java.io.Serializable

data class TrackDto(
    val trackId: Int,
    val trackName: String? = null, // Название композиции
    val artistName: String? = null, // Имя исполнителя
    val trackTimeMillis: Int? = 0, // Продолжительность трека: миллисекунды
    val artworkUrl100: String? = null, // Ссылка на изображение обложки
    val collectionName: String? = null, // Название альбома
    val releaseDate: String? = null, // Год выпуска трека
    val primaryGenreName: String? = null, // жанр
    val country: String? = null, // Страна исполнителя
    val previewUrl: String? = null, // отрывок трека
) : Serializable