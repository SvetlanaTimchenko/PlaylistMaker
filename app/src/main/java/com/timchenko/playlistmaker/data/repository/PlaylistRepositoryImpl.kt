package com.timchenko.playlistmaker.data.repository

import com.timchenko.playlistmaker.data.ImageStorage
import com.timchenko.playlistmaker.data.db.AppDatabase
import com.timchenko.playlistmaker.data.db.entity.PlaylistEntity
import com.timchenko.playlistmaker.data.db.entity.PlaylistTracksEntity
import com.timchenko.playlistmaker.data.mapper.PlaylistMapper
import com.timchenko.playlistmaker.data.mapper.PlaylistTracksMapper
import com.timchenko.playlistmaker.data.mapper.TrackMapper
import com.timchenko.playlistmaker.domain.models.Playlist
import com.timchenko.playlistmaker.domain.models.PlaylistTracks
import com.timchenko.playlistmaker.domain.models.Track
import com.timchenko.playlistmaker.domain.repository.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistMapper: PlaylistMapper,
    private val storage: ImageStorage,
    private val playlistTracksMapper: PlaylistTracksMapper,
    private val trackMapper: TrackMapper
) : PlaylistRepository {
    override suspend fun getAll(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.getPlaylistDao().getAll()
        playlists?.let { convertFromPlaylistEntity(it) }?.let { emit(it) }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>) : List<Playlist> {
        return playlists.map { playlistMapper.map(it) }
    }

    override suspend fun add(playlist: Playlist) {
        playlist.uri = saveImage(playlist.uri)
        appDatabase.getPlaylistDao().add(playlistMapper.map(playlist))
    }

    override suspend fun update(playlist: Playlist) {
        playlist.uri = saveImage(playlist.uri)
        appDatabase.getPlaylistDao().update(playlistMapper.map(playlist))
    }

    private fun saveImage(imagePath: String?): String {
        return if(imagePath?.isEmpty() == false) {
            storage.saveImageInPrivateStorage(imagePath)
        } else {
            ""
        }
    }

    override suspend fun delete(playlistId: Int) {
        appDatabase.getPlaylistDao().delete(playlistId)
    }
    override suspend fun getById(id: Int): Flow<Playlist> = flow {
        val item =  appDatabase.getPlaylistDao().getById(id)
        emit(playlistMapper.map(item))
    }

    override suspend fun addPlaylistTracks(playlistTracks: PlaylistTracks) {
        appDatabase.getPlaylistTracksDao().addTrack(playlistTracksMapper.map(playlistTracks))
    }

    override suspend fun deletePlaylistTrack(playlistId: Int, trackId: Int) {
        val playlistTrackEntity: PlaylistTracksEntity = appDatabase.getPlaylistTracksDao().get(playlistId, trackId)
        var playlistEntity: PlaylistEntity = appDatabase.getPlaylistDao().getById(playlistId)
        val playlist: Playlist = playlistMapper.map(playlistEntity)

        if (playlist.tracks.contains(trackId)) {
            playlist.tracks.remove(trackId)
            playlist.tracksCounter--
            playlist.trackTimerMillis = playlist.trackTimerMillis - playlistTrackEntity.trackTimeMillis!!
        }
        playlistEntity = playlistMapper.map(playlist)

        appDatabase.getPlaylistDao().update(playlistEntity)
        appDatabase.getPlaylistTracksDao().deleteTrack(playlistId, trackId)
    }

    override suspend fun getPlaylistTracks(playlistId: Int): Flow<List<Track>> = flow{
        val tracks = appDatabase.getPlaylistTracksDao().getByPlaylist(playlistId)?.map {
            trackMapper.map(it)
        }
        val favorites = appDatabase.getFavoriteDao().getIds()
        if (favorites != null && tracks != null) {
            setFavoritesToTracks(tracks, favorites)
        }
        if (tracks != null) emit(tracks)

    }
    private fun setFavoritesToTracks(tracks: List<Track>, indicators: List<Int>) {
        for (i in tracks) {
            if (i.trackId in indicators) {
                i.isFavorite = true
            }
        }
    }
}