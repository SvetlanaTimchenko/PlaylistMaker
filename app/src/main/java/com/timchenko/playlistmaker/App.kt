package com.timchenko.playlistmaker

import android.app.Application
import com.markodevcic.peko.PermissionRequester
import com.timchenko.playlistmaker.di.dataModule
import com.timchenko.playlistmaker.di.interactorModule
import com.timchenko.playlistmaker.di.repositoryModule
import com.timchenko.playlistmaker.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }

        PermissionRequester.initialize(applicationContext)
    }
}