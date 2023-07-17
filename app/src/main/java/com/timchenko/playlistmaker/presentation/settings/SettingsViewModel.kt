package com.timchenko.playlistmaker.presentation.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.timchenko.playlistmaker.domain.models.ThemeSettings
import com.timchenko.playlistmaker.util.Creator

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val settingsInteractor by lazy { Creator.provideSettingsInteractor(context = getApplication<Application>()) }
    private val sharingInteractor by lazy { Creator.provideSharingInteractor(context = getApplication<Application>())}

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

    companion object {

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(this[APPLICATION_KEY] as Application)
            }
        }
    }

}