package com.timchenko.playlistmaker.data

import com.timchenko.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response
}