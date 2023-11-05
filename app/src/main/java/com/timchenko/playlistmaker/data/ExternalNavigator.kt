package com.timchenko.playlistmaker.data

import com.timchenko.playlistmaker.domain.models.EmailData


interface ExternalNavigator {
    fun shareLink(url: String, title: String)
    fun sharePlaylist(message: String)
    fun openLink(url: String)
    fun openEmail(data: EmailData)
}