package com.timchenko.playlistmaker.data.mapper

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.timchenko.playlistmaker.data.db.entity.PlaylistEntity
import com.timchenko.playlistmaker.domain.models.Playlist

class PlaylistMapper {

    private val gson = Gson()
    fun map(playlistEntity: PlaylistEntity): Playlist {
        var  tracks: ArrayList<Int> = ArrayList()
        if (playlistEntity.tracks is String) {
            tracks = convertFromGson(playlistEntity.tracks)
        }
        return Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            uri = playlistEntity.uri,
            tracks = tracks,
            tracksCounter = playlistEntity.tracksCounter
        )
    }

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            uri = playlist.uri,
            tracks = convertToGson(playlist.tracks),
            tracksCounter = playlist.tracksCounter
        )
    }

    private fun convertToGson(trackIds: ArrayList<Int>): String {
        return gson.toJson(trackIds)
    }

    private fun convertFromGson(json: String): ArrayList<Int> {
        val itemType = object : TypeToken<ArrayList<Int>>() {}.type
        return gson.fromJson(json, itemType)
    }
}