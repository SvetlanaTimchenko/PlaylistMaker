package com.timchenko.playlistmaker.domain.repository

import com.timchenko.playlistmaker.domain.models.ThemeSettings


interface SettingsRepository {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}