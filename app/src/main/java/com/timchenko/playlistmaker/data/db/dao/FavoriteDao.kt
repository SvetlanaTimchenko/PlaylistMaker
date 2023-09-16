package com.timchenko.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timchenko.playlistmaker.data.db.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(entity = FavoriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun add(track: FavoriteEntity)

    @Delete(entity = FavoriteEntity::class)
    suspend fun delete(track: FavoriteEntity)

    @Query("SELECT * FROM favorite_entity ORDER BY insertTimeStamp DESC")
    suspend fun getAll(): List<FavoriteEntity>?

    @Query("SELECT trackId FROM favorite_entity")
    suspend fun getIds(): List<Int>?

}