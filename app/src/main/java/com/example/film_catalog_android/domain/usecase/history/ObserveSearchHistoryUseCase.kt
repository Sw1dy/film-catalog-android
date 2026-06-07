package com.example.film_catalog_android.domain.usecase.history

import com.example.film_catalog_android.domain.model.SearchHistoryItem
import com.example.film_catalog_android.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow

class ObserveSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    operator fun invoke(): Flow<List<SearchHistoryItem>> {
        return searchHistoryRepository.observeHistory()
    }
}