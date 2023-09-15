package com.timchenko.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_entity")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true)
    val trackId: Int,
    val trackName: String? = "test"
)