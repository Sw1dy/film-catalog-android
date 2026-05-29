package com.example.film_catalog_android.domain.repository

import com.example.film_catalog_android.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface WatchListRepository {

    fun observeWatchList(): Flow<List<Movie>>

    fun observeWatchListIds(): Flow<Set<Long>>

    suspend fun toggleMovie(movie: Movie)

    suspend fun removeMovie(movieId: Long)
}