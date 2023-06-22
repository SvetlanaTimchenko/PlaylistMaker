package com.timchenko.playlistmaker

import com.timchenko.playlistmaker.domain.models.Track

class ITunesResponse(
    val resultCount: Int,
    val results: List<Track>)
