package com.example.film_catalog_android.domain.usecase.watchlist

import com.example.film_catalog_android.domain.repository.WatchListRepository
import kotlinx.coroutines.flow.Flow

class ObserveWatchListIdsUseCase(
    private val watchListRepository: WatchListRepository
) {
    operator fun invoke(): Flow<Set<Long>> {
        return watchListRepository.observeWatchListIds()
    }
}