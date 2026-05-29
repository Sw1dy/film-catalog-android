package com.example.film_catalog_android.data.repository

import com.example.film_catalog_android.data.mapper.toCreateRequest
import com.example.film_catalog_android.data.mapper.toDomain
import com.example.film_catalog_android.data.mapper.toUpdateRequest
import com.example.film_catalog_android.data.remote.MovieApi
import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RemoteMovieRepository(
    private val movieApi: MovieApi = MovieApi()
) : MovieRepository {

    private val moviesFlow = MutableStateFlow<List<Movie>>(emptyList())

    override fun observeMovies(): Flow<List<Movie>> {
        return moviesFlow.asStateFlow()
    }

    override suspend fun getMovies(): List<Movie> {
        val movies = movieApi.getMovies().map { dto ->
            dto.toDomain()
        }

        moviesFlow.value = movies

        return movies
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        return movieApi.searchMovies(query).map { dto ->
            dto.toDomain()
        }
    }

    override suspend fun getMovieById(id: Long): Movie? {
        return try {
            movieApi.getMovieById(id).toDomain()
        } catch (exception: Exception) {
            null
        }
    }

    override suspend fun addMovie(movie: Movie) {
        movieApi.addMovie(movie.toCreateRequest())
        getMovies()
    }

    override suspend fun updateMovie(movie: Movie) {
        movieApi.updateMovie(
            id = movie.id,
            request = movie.toUpdateRequest()
        )
        getMovies()
    }

    override suspend fun deleteMovie(movieId: Long) {
        movieApi.deleteMovie(movieId)
        getMovies()
    }
}