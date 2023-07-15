package com.timchenko.playlistmaker.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.timchenko.playlistmaker.domain.models.ThemeSettings
import com.timchenko.playlistmaker.domain.repository.SettingsRepository

class SettingsRepositoryImpl(context: Context) : SettingsRepository {

    companion object {
        const val SHARED_PREFS = "playlist_maker"
        const val NIGHT_THEME = "dark_mode"
    }

    private var sharedPrefs = context.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(darkTheme = sharedPrefs.getInt(NIGHT_THEME, -1))
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        if (settings.darkTheme > 0) sharedPrefs.edit().putInt(NIGHT_THEME, settings.darkTheme).apply()
        setDarkMode(settings)
    }

     private fun setDarkMode(settings: ThemeSettings) {
        AppCompatDelegate.setDefaultNightMode(
            when (settings.darkTheme) {
                2 -> AppCompatDelegate.MODE_NIGHT_YES
                1 -> AppCompatDelegate.MODE_NIGHT_NO
                else -> {
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            }
        )
    }
}