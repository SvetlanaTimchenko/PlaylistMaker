package com.timchenko.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.timchenko.playlistmaker.data.ExternalNavigator
import com.timchenko.playlistmaker.data.NetworkClient
import com.timchenko.playlistmaker.data.impl.ExternalNavigatorImpl
import com.timchenko.playlistmaker.data.network.ITunesApi
import com.timchenko.playlistmaker.data.network.RetrofitNetworkClient
import com.timchenko.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.timchenko.playlistmaker.domain.repository.SearchHistoryRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("playlist_maker", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    factory { MediaPlayer() }

    single<NetworkClient> {
        RetrofitNetworkClient(get(), androidContext())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get(), get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }
}