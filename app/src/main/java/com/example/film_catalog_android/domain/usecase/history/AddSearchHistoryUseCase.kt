package com.example.film_catalog_android.domain.usecase.history

import com.example.film_catalog_android.domain.repository.SearchHistoryRepository

class AddSearchHistoryUseCase(
    private val searchHistoryRepository: SearchHistoryRepository
) {
    suspend operator fun invoke(movieId: Long, title: String) {
        searchHistoryRepository.addToHistory(
            movieId = movieId,
            title = title
        )
    }
}