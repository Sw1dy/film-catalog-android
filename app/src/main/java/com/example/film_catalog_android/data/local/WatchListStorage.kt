package com.example.film_catalog_android.data.local

import com.example.film_catalog_android.domain.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object WatchListStorage {

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    fun toggleMovie(movie: Movie) {
        val currentMovies = _movies.value
        val exists = currentMovies.any { it.id == movie.id }

        _movies.value = if (exists) {
            currentMovies.filterNot { it.id == movie.id }
        } else {
            listOf(movie.copy(isInWatchlist = true)) + currentMovies
        }
    }

    fun isMovieInWatchList(movieId: Long): Boolean {
        return _movies.value.any { it.id == movieId }
    }

    fun removeMovie(movieId: Long) {
        _movies.value = _movies.value.filterNot { it.id == movieId }
    }
}

//Временное решение