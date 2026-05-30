package com.example.film_catalog_android.data.repository

import com.example.film_catalog_android.data.local.UserSessionStorage
import com.example.film_catalog_android.data.local.dao.SearchHistoryDao
import com.example.film_catalog_android.data.local.entity.SearchHistoryEntity
import com.example.film_catalog_android.domain.model.SearchHistoryItem
import com.example.film_catalog_android.domain.repository.SearchHistoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class SearchHistoryRepositoryImpl(
    private val dao: SearchHistoryDao
) : SearchHistoryRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeHistory(): Flow<List<SearchHistoryItem>> {
        return UserSessionStorage.userId.flatMapLatest { userId ->
            if (userId == null) {
                flowOf(emptyList())
            } else {
                dao.observeHistory(userId).map { entities ->
                    entities.map { entity ->
                        entity.toDomain()
                    }
                }
            }
        }
    }

    override suspend fun addToHistory(movieId: Long, title: String) {
        val userId = UserSessionStorage.userId.first() ?: return

        dao.deleteByMovieId(
            userId = userId,
            movieId = movieId
        )

        dao.insert(
            SearchHistoryEntity(
                userId = userId,
                movieId = movieId,
                title = title,
                createdAt = System.currentTimeMillis()
            )
        )

        dao.deleteOldItems(userId)
    }

    override suspend fun clearHistory() {
        val userId = UserSessionStorage.userId.first() ?: return
        dao.clearHistory(userId)
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
