package com.timchenko.playlistmaker.presentation.settings

import androidx.lifecycle.ViewModel
import com.timchenko.playlistmaker.domain.SettingsInteractor
import com.timchenko.playlistmaker.domain.SharingInteractor
import com.timchenko.playlistmaker.domain.models.ThemeSettings

class SettingsViewModel(
    private val settingsInteractor: SettingsInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    fun updateThemeSettings(checked: Boolean) {
        if (checked) {
            settingsInteractor.updateThemeSetting(ThemeSettings.DARK)
        }
        else {
            settingsInteractor.updateThemeSetting(ThemeSettings.LIGHT)
        }
    }

    fun shareApp(url: String, title: String) {
        sharingInteractor.shareApp(link = url, title = title)
    }

    fun openSupport(email: String, subject: String, text: String) {
        sharingInteractor.openSupport(email = email, subject = subject, text = text)
    }

    fun openTerms(url: String) {
        sharingInteractor.openTerms(link = url)
    }
}