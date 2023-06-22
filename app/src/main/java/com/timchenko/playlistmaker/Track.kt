package com.timchenko.playlistmaker

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Track(
    val trackId: Int,
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    @SerializedName("trackTimeMillis") val trackTime: Int, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название альбома
    val releaseDate: String, // Год релиза трека
    val primaryGenreName: String, // жанр
    val country: String, // Страна исполнителя
    val previewUrl: String, // отрывок трека
) : Serializable {
    /**
     * Возвращает измененный URL обложки размером 512х512
     */
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}
