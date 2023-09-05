package com.timchenko.playlistmaker.ui.audioplayer

import com.timchenko.playlistmaker.R
sealed class PlayerState (val isPlayButtonEnabled: Boolean, val buttonResource: Int, val progress: String) {
    class Default : PlayerState(false, R.drawable.buttonplay, "00:00")
    class Prepared : PlayerState(true, R.drawable.buttonplay, "00:00")
    class Playing(progress: String) : PlayerState(true, R.drawable.buttonpause, progress)
    class Paused(progress: String) : PlayerState(true, R.drawable.buttonplay, progress)
}
