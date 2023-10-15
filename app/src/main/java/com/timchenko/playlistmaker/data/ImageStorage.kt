package com.timchenko.playlistmaker.data

interface ImageStorage {
    fun saveImageInPrivateStorage(uri: String): String
}