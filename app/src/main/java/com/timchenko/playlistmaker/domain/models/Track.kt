package com.timchenko.playlistmaker.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track(
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
    var isFavorite: Boolean = false
) : Parcelable {
    /**
     * Возвращает обновленный URL обложки размером 512х512
     */
    fun getCoverArtwork() = artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
    fun getCoverArtworkForPlaylist() = artworkUrl100?.replaceAfterLast('/', "60x60bb.jpg")
}
