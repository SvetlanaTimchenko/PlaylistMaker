package com.timchenko.playlistmaker.domain

import com.timchenko.playlistmaker.domain.models.ThemeSettings

interface SettingsInteractor {

    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)

}