package com.timchenko.playlistmaker.domain.models

import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String? = null, // Название композиции
    val artistName: String? = null, // Имя исполнителя
    val trackTime: String? = null, // Продолжительность трека в формате mm:ss
    val artworkUrl100: String? = null, // Ссылка на изображение обложки
    val collectionName: String? = null, // Название альбома
    val releaseDate: String? = null, // Год выпуска трека
    val primaryGenreName: String? = null, // жанр
    val country: String? = null, // Страна исполнителя
    val previewUrl: String? = null, // отрывок трека
) : Serializable
