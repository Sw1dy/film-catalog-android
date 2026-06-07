package com.example.film_catalog_android.domain.usecase.movie

import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository

class SearchMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(query: String): List<Movie> {
        return movieRepository.searchMovies(query)
    }
}