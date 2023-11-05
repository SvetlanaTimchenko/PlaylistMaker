package com.timchenko.playlistmaker.domain

interface SharingInteractor {
    fun shareApp(link: String, title: String)
    fun sharePlaylist(message: String)
    fun openTerms(link: String)
    fun openSupport(email: String, subject: String, text: String)
}