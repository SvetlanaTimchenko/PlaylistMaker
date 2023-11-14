package com.timchenko.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.timchenko.playlistmaker.data.db.dao.FavoriteDao
import com.timchenko.playlistmaker.data.db.dao.PlaylistDao
import com.timchenko.playlistmaker.data.db.dao.PlaylistTracksDao
import com.timchenko.playlistmaker.data.db.entity.FavoriteEntity
import com.timchenko.playlistmaker.data.db.entity.PlaylistEntity
import com.timchenko.playlistmaker.data.db.entity.PlaylistTracksEntity

@Database(version = 7, entities = [FavoriteEntity::class, PlaylistEntity::class, PlaylistTracksEntity::class], exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getFavoriteDao(): FavoriteDao
    abstract fun getPlaylistDao(): PlaylistDao
    abstract fun getPlaylistTracksDao(): PlaylistTracksDao
}