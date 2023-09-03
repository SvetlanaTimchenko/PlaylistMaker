package com.timchenko.playlistmaker.data.network

import com.timchenko.playlistmaker.data.dto.ITunesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {

    @GET("/search?entity=song")
    suspend fun search(@Query("term") text: String): ITunesResponse
}