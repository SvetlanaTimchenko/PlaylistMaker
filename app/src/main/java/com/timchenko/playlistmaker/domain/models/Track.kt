package com.timchenko.playlistmaker.domain.models

import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTime: String, // Продолжительность трека в формате mm:ss
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название альбома
    val releaseDate: String, // Год выпуска трека
    val primaryGenreName: String, // жанр
    val country: String, // Страна исполнителя
    val previewUrl: String, // отрывок трека
) : Serializable
