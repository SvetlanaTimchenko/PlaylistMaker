package com.timchenko.playlistmaker.domain.models

enum class ThemeSettings(val darkTheme: Int) {
    LIGHT(1),
    DARK(2),
    SYSTEM_DEFAULT(-1)
}
