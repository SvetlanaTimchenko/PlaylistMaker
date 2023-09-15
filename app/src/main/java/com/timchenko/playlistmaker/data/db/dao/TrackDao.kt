package com.timchenko.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timchenko.playlistmaker.data.db.entity.FavoriteEntity

@Dao
interface TrackDao {
    @Insert(entity = FavoriteEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(track: FavoriteEntity)

    @Delete(entity = FavoriteEntity::class)
    fun deleteTrack(track: FavoriteEntity)

    @Query("SELECT * FROM favorite_entity")
    fun getAll(): List<FavoriteEntity>

    @Query("SELECT trackId FROM favorite_entity")
    fun getIds(): List<FavoriteEntity>

}