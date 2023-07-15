package com.timchenko.playlistmaker.domain.impl

import com.timchenko.playlistmaker.domain.TracksInteractor
import com.timchenko.playlistmaker.domain.repository.TracksRepository
import com.timchenko.playlistmaker.util.Resource

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = java.util.concurrent.Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when(val resource = repository.searchTracks(expression)) {
                is Resource.Success -> { consumer.consume(resource.data, null) }
                is Resource.Error -> { consumer.consume(null, resource.message) }
            }
        }
    }
}