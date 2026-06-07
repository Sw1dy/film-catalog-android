package com.example.film_catalog_android.domain.usecase.movie

import com.example.film_catalog_android.domain.repository.MovieRepository

class GetMovieGenresUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): List<String> {
        return movieRepository.getGenres()
    }
}