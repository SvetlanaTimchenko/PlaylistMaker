package com.timchenko.playlistmaker.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.timchenko.playlistmaker.domain.models.ThemeSettings
import com.timchenko.playlistmaker.domain.repository.SettingsRepository

class SettingsRepositoryImpl(
    private var sharedPrefs: SharedPreferences
) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        val theme = sharedPrefs.getInt(NIGHT_THEME, -1)
        return getThemeFromInt(theme) ?: ThemeSettings.SYSTEM_DEFAULT
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

    companion object {
        const val NIGHT_THEME = "dark_mode"
        private val map = ThemeSettings.values().associateBy(ThemeSettings::darkTheme)
        fun getThemeFromInt(type: Int) = map[type]
    }
}