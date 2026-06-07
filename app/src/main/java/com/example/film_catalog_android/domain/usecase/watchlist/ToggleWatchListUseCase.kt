package com.example.film_catalog_android.domain.usecase.watchlist

import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.WatchListRepository

class ToggleWatchListUseCase(
    private val watchListRepository: WatchListRepository
) {
    suspend operator fun invoke(movie: Movie) {
        watchListRepository.toggleMovie(movie)
    }
}