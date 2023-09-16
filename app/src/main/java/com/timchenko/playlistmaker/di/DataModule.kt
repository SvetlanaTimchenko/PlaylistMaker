package com.timchenko.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.timchenko.playlistmaker.data.ExternalNavigator
import com.timchenko.playlistmaker.data.NetworkClient
import com.timchenko.playlistmaker.data.db.AppDatabase
import com.timchenko.playlistmaker.data.impl.ExternalNavigatorImpl
import com.timchenko.playlistmaker.data.network.ITunesApi
import com.timchenko.playlistmaker.data.network.RetrofitNetworkClient
import com.timchenko.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import com.timchenko.playlistmaker.domain.repository.SearchHistoryRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ITUNES_URL = "https://itunes.apple.com"

val dataModule = module {
    single<ITunesApi> {
        Retrofit.Builder()
            .baseUrl(ITUNES_URL)
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
        SearchHistoryRepositoryImpl(get(), get(), get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(get())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "db.db").build()
    }
}