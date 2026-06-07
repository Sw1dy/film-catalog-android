package com.example.film_catalog_android.domain.usecase.history

import com.example.film_catalog_android.domain.repository.SearchHistoryRepository

class ClearSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke() {
        searchHistoryRepository.clearHistory()
    }
}