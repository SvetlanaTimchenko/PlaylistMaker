package com.timchenko.playlistmaker

import android.app.Application
import com.timchenko.playlistmaker.domain.models.ThemeSettings
import com.timchenko.playlistmaker.util.Creator

class App : Application() {

    private lateinit var systemSettings: ThemeSettings
    private val settingsInteractor by lazy { Creator.provideSettingsInteractor(context = applicationContext) }

    override fun onCreate() {
        super.onCreate()

        systemSettings = settingsInteractor.getThemeSettings()
        settingsInteractor.updateThemeSetting(systemSettings)
    }
}