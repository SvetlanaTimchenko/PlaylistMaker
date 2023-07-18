package com.timchenko.playlistmaker.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val ITUNES_URL = "https://itunes.apple.com"

    private val client: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(ITUNES_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ITunesApi by lazy {
        client.create(ITunesApi::class.java)
    }


}
