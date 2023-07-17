package com.timchenko.playlistmaker.data.dto

import java.io.Serializable

data class TrackDto(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека: миллисекунды
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название альбома
    val releaseDate: String, // Год выпуска трека
    val primaryGenreName: String, // жанр
    val country: String, // Страна исполнителя
    val previewUrl: String, // отрывок трека
) : Serializable