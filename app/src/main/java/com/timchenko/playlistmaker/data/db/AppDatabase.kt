package com.timchenko.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.timchenko.playlistmaker.data.db.dao.TrackDao
import com.timchenko.playlistmaker.data.db.entity.FavoriteEntity

@Database(version = 1, entities = [FavoriteEntity::class])
abstract class AppDatabase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}