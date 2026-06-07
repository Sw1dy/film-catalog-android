package com.example.film_catalog_android.domain.usecase.movie

import com.example.film_catalog_android.domain.repository.MovieRepository

class DeleteMovieUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Long) {
        movieRepository.deleteMovie(movieId)
    }
}
