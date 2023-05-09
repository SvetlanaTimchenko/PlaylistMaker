package com.timchenko.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sharedPrefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE)

        when (sharedPrefs.getString(NIGHT_THEME, "")) {
            "dark" -> switchTheme(true)
            "light" -> switchTheme(false)
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
        // записываем в shared preferences
        if (darkThemeEnabled) {
            sharedPrefs.edit()
                .putString(NIGHT_THEME, "dark")
                .apply()
        } else {
            sharedPrefs.edit()
                .putString(NIGHT_THEME, "light")
                .apply()
        }
    }
}