package com.example.film_catalog_android.domain.usecase.movie

import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class ObserveMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return movieRepository.observeMovies()
    }
}
