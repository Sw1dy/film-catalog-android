package com.example.film_catalog_android.data.repository

import com.example.film_catalog_android.data.local.dao.WatchListDao
import com.example.film_catalog_android.data.local.entity.WatchListEntity
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import com.example.film_catalog_android.domain.repository.WatchListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchListRepositoryImpl(
    private val watchListDao: WatchListDao,
    private val movieRepository: MovieRepository
) : WatchListRepository {

    override fun observeWatchList(): Flow<List<Movie>> {
        return watchListDao.observeWatchList().map { entities ->
            entities.mapNotNull { entity ->
                movieRepository.getMovieById(entity.movieId)
            }
        }
    }

    override fun observeWatchListIds(): Flow<Set<Long>> {
        return watchListDao.observeWatchList().map { entities ->
            entities.map { entity ->
                entity.movieId
            }.toSet()
        }
    }

    override suspend fun toggleMovie(movie: Movie) {
        val isInWatchList = watchListDao.isInWatchList(movie.id)

        if (isInWatchList) {
            watchListDao.delete(movie.id)
        } else {
            watchListDao.insert(
                WatchListEntity(
                    movieId = movie.id,
                    addedAt = System.currentTimeMillis()
                )
            )
        }
    }

    override suspend fun removeMovie(movieId: Long) {
        watchListDao.delete(movieId)
    }
}