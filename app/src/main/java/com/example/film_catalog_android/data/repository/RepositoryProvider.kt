package com.example.film_catalog_android.data.repository

import com.example.film_catalog_android.domain.repository.MovieRepository

object RepositoryProvider {

    val movieRepository: MovieRepository by lazy {
        RemoteMovieRepository()
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository()
    }
}
