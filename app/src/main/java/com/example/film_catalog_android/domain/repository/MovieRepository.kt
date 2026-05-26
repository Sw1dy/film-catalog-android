package com.example.film_catalog_android.domain.repository

import com.example.film_catalog_android.domain.model.Movie

interface MovieRepository {

    suspend fun getMovies(): List<Movie>

    suspend fun searchMovies(query: String): List<Movie>

    suspend fun getMovieById(id: Long): Movie?
}