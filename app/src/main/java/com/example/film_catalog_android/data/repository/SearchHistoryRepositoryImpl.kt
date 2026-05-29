package com.example.film_catalog_android.data.repository

import com.example.film_catalog_android.data.local.dao.SearchHistoryDao
import com.example.film_catalog_android.data.local.entity.SearchHistoryEntity
import com.example.film_catalog_android.domain.model.SearchHistoryItem
import com.example.film_catalog_android.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchHistoryRepositoryImpl(
    private val dao: SearchHistoryDao
) : SearchHistoryRepository {

    override fun observeHistory(): Flow<List<SearchHistoryItem>> {
        return dao.observeHistory().map { entities ->
            entities.map { entity ->
                entity.toDomain()
            }
        }
    }

    override suspend fun addToHistory(movieId: Long, title: String) {
        dao.deleteByMovieId(movieId)

        dao.insert(
            SearchHistoryEntity(
                movieId = movieId,
                title = title,
                createdAt = System.currentTimeMillis()
            )
        )

        dao.deleteOldItems()
    }

    override suspend fun clearHistory() {
        dao.clearHistory()
    }

    private fun SearchHistoryEntity.toDomain(): SearchHistoryItem {
        return SearchHistoryItem(
            id = id,
            movieId = movieId,
            title = title,
            createdAt = createdAt
        )
    }
}