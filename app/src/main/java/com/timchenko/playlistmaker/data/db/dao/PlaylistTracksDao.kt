package com.timchenko.playlistmaker.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.timchenko.playlistmaker.data.db.entity.PlaylistTracksEntity

@Dao
interface PlaylistTracksDao {
    @Insert(entity = PlaylistTracksEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(playlistTracks: PlaylistTracksEntity)

    @Query("SELECT * FROM playlist_tracks_entity WHERE playlistId=:id ORDER BY id DESC")
    suspend fun getByPlaylist(id: Int): List<PlaylistTracksEntity>?

    @Query("DELETE FROM playlist_tracks_entity WHERE playlistId=:playlistId AND trackId=:trackId")
    suspend fun deleteTrack(playlistId: Int, trackId: Int)

    @Query("DELETE FROM playlist_tracks_entity WHERE trackId=:trackId")
    suspend fun deleteTracks(trackId: Int)

    @Query("SELECT * FROM playlist_tracks_entity WHERE playlistId=:playlistId AND trackId=:trackId")
    suspend fun get(playlistId: Int, trackId: Int): PlaylistTracksEntity

}