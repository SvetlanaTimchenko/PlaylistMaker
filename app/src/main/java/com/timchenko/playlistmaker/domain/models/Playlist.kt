package com.timchenko.playlistmaker.domain.models

data class Playlist(
    val id: Int? = null, // ID плейлиста
    var name: String? = null, // название
    var description: String? = null, // описание
    var uri: String? = null, // путь к обложке
    val tracks: ArrayList<Int>, // список треков
    var trackTimerMillis: Int = 0
)
