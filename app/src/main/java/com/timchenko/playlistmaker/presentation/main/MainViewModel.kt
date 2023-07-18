package com.timchenko.playlistmaker.presentation.main

import androidx.lifecycle.ViewModel
import com.timchenko.playlistmaker.domain.SettingsInteractor

class MainViewModel(
    private val settingsInteractor: SettingsInteractor
): ViewModel() {

    fun setAppTheme() {
        val systemSettings = settingsInteractor.getThemeSettings()
        settingsInteractor.updateThemeSetting(systemSettings)
    }
}