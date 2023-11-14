package com.timchenko.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_entity")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String?,
    val description: String?,
    val uri: String?,
    val tracks: String?,
    val tracksTimerMillis: Int
)