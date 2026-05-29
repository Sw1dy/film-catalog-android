package com.example.film_catalog_android.domain.repository

import com.example.film_catalog_android.domain.model.SearchHistoryItem
import kotlinx.coroutines.flow.Flow

interface SearchHistoryRepository {

    fun observeHistory(): Flow<List<SearchHistoryItem>>

    suspend fun addToHistory(movieId: Long, title: String)

    suspend fun clearHistory()
}