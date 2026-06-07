package com.example.film_catalog_android.domain.usecase.movie

import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository

class GetMoviesUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(
        genre: String? = null,
        year: Int? = null
    ): List<Movie> {
        return movieRepository.getMovies(
            genre = genre,
            year = year
        )
    }
}