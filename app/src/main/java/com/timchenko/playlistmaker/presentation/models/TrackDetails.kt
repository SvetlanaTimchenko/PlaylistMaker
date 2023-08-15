package com.timchenko.playlistmaker.presentation.models

import java.io.Serializable

data class TrackDetails(
    val trackId: Int,
    val trackName: String? = null, // Название композиции
    val artistName: String? = null, // Имя исполнителя
    val trackTime: String? = null, // Продолжительность трека в формате mm:ss
    val artworkUrl100: String? = null, // Ссылка на изображение обложки
    val collectionName: String? = null, // Название альбома
    val releaseYear: String? = null, // Год выпуска трека
    val primaryGenreName: String? = null, // жанр
    val country: String? = null, // Страна исполнителя
    val previewUrl: String? = null, // отрывок трека
) : Serializable {
    /**
     * Возвращает обновленный URL обложки размером 512х512
     */
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/',"512x512bb.jpg")
}