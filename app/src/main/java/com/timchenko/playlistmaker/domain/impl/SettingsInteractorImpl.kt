package com.timchenko.playlistmaker.domain.impl

import com.timchenko.playlistmaker.domain.SettingsInteractor
import com.timchenko.playlistmaker.domain.models.ThemeSettings
import com.timchenko.playlistmaker.domain.repository.SettingsRepository

class SettingsInteractorImpl(
    private val settingsRepository: SettingsRepository
): SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingsRepository.updateThemeSetting(settings)
    }
}