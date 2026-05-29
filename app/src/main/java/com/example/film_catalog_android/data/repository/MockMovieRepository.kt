package com.example.film_catalog_android.data.repository

import com.example.film_catalog_android.data.local.MovieStorage
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class MockMovieRepository : MovieRepository {

    override fun observeMovies(): Flow<List<Movie>> {
        return MovieStorage.movies
    }

    override suspend fun getMovies(): List<Movie> {
        delay(500)
        return MovieStorage.getMovies()
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        delay(250)
        return MovieStorage.searchMovies(query)
    }

    override suspend fun getMovieById(id: Long): Movie? {
        delay(300)
        return MovieStorage.getMovieById(id)
    }

    override suspend fun addMovie(movie: Movie) {
        delay(300)
        MovieStorage.addMovie(movie)
    }

    override suspend fun updateMovie(movie: Movie) {
        delay(300)
        MovieStorage.updateMovie(movie)
    }

    override suspend fun deleteMovie(movieId: Long) {
        delay(300)
        MovieStorage.deleteMovie(movieId)
    }
}