package com.timchenko.playlistmaker.presentation.root

import androidx.lifecycle.ViewModel
import com.timchenko.playlistmaker.domain.SettingsInteractor

class RootViewModel(private val settingsInteractor: SettingsInteractor): ViewModel() {
    fun setAppTheme() {
        val systemSettings = settingsInteractor.getThemeSettings()
        settingsInteractor.updateThemeSetting(systemSettings)
    }
}