package com.example.film_catalog_android.data.repository

import com.example.film_catalog_android.data.local.UserSessionStorage
import com.example.film_catalog_android.data.local.dao.WatchListDao
import com.example.film_catalog_android.data.local.entity.WatchListEntity
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import com.example.film_catalog_android.domain.repository.WatchListRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class WatchListRepositoryImpl(
    private val watchListDao: WatchListDao,
    private val movieRepository: MovieRepository
) : WatchListRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeWatchList(): Flow<List<Movie>> {
        return UserSessionStorage.userId.flatMapLatest { userId ->
            if (userId == null) {
                flowOf(emptyList())
            } else {
                watchListDao.observeWatchList(userId).map { entities ->
                    entities.map { entity ->
                        entity.toDomain()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeWatchListIds(): Flow<Set<Long>> {
        return UserSessionStorage.userId.flatMapLatest { userId ->
            if (userId == null) {
                flowOf(emptySet())
            } else {
                watchListDao.observeWatchListIds(userId).map { movieIds ->
                    movieIds.toSet()
                }
            }
        }
    }

    override suspend fun toggleMovie(movie: Movie) {
        val userId = UserSessionStorage.userId.first() ?: return

        val isInWatchList = watchListDao.isMovieInWatchList(
            userId = userId,
            movieId = movie.id
        )

        if (isInWatchList) {
            watchListDao.delete(userId, movie.id)
        } else {
            watchListDao.insert(
                movie.toWatchListEntity(userId)
            )
        }
    }

    override suspend fun removeMovie(movieId: Long) {
        val userId = UserSessionStorage.userId.first() ?: return
        watchListDao.delete(userId, movieId)
    }

    private fun WatchListEntity.toDomain(): Movie {
        return Movie(
            id = movieId,
            title = title,
            description = description,
            year = year,
            genre = genre,
            rating = rating,
            imageUrl = imageUrl,
            isInWatchlist = true
        )
    }

    private fun Movie.toWatchListEntity(userId: Long): WatchListEntity {
        return WatchListEntity(
            userId = userId,
            movieId = id,
            title = title,
            description = description,
            year = year,
            genre = genre,
            rating = rating,
            imageUrl = imageUrl
        )
    }
}