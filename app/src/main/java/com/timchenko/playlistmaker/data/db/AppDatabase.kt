package com.timchenko.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.timchenko.playlistmaker.data.db.dao.FavoriteDao
import com.timchenko.playlistmaker.data.db.dao.PlaylistDao
import com.timchenko.playlistmaker.data.db.entity.FavoriteEntity
import com.timchenko.playlistmaker.data.db.entity.PlaylistEntity

@Database(version = 2, entities = [FavoriteEntity::class, PlaylistEntity::class], exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getFavoriteDao(): FavoriteDao
    abstract fun gerPlaylistDao(): PlaylistDao
}