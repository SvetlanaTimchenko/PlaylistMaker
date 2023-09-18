package com.timchenko.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.timchenko.playlistmaker.data.db.dao.FavoriteDao
import com.timchenko.playlistmaker.data.db.entity.FavoriteEntity

@Database(version = 1, entities = [FavoriteEntity::class], exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getFavoriteDao(): FavoriteDao
}