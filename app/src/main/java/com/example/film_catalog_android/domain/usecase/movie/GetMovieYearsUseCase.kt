package com.example.film_catalog_android.domain.usecase.movie

import com.example.film_catalog_android.domain.repository.MovieRepository

class GetMovieYearsUseCase(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): List<Int> {
        return movieRepository.getYears()
    }
}