package com.example.film_catalog_android.domain.usecase.watchlist

import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.WatchListRepository
import kotlinx.coroutines.flow.Flow

class ObserveWatchListUseCase(
    private val watchListRepository: WatchListRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return watchListRepository.observeWatchList()
    }
}