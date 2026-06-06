package com.example.film_catalog_android.domain.repository

import com.example.film_catalog_android.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun observeMovies(): Flow<List<Movie>>

    suspend fun getMovies(
        genre: String? = null,
        year: Int? = null
    ): List<Movie>

    suspend fun getGenres(): List<String>

    suspend fun getYears(): List<Int>

    suspend fun searchMovies(query: String): List<Movie>

    suspend fun getMovieById(id: Long): Movie?

    suspend fun addMovie(movie: Movie)

    suspend fun updateMovie(movie: Movie)

    suspend fun deleteMovie(movieId: Long)
}