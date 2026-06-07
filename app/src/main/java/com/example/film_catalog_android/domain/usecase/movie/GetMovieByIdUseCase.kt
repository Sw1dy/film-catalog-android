package com.example.film_catalog_android.domain.usecase.movie

import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.repository.MovieRepository

class GetMovieByIdUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long): Movie? {
        return movieRepository.getMovieById(movieId)
    }
}