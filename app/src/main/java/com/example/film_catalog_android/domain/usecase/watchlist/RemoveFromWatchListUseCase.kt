package com.example.film_catalog_android.domain.usecase.watchlist

import com.example.film_catalog_android.domain.repository.WatchListRepository

class RemoveFromWatchListUseCase(
    private val watchListRepository: WatchListRepository
) {
    suspend operator fun invoke(movieId: Long) {
        watchListRepository.removeMovie(movieId)
    }
}