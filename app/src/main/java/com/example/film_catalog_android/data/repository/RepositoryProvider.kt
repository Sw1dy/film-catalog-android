package com.example.film_catalog_android.data.repository

import com.example.film_catalog_android.data.local.DatabaseProvider
import com.example.film_catalog_android.domain.repository.AuthRepository
import com.example.film_catalog_android.domain.repository.MovieRepository
import com.example.film_catalog_android.domain.repository.SearchHistoryRepository
import com.example.film_catalog_android.domain.repository.WatchListRepository

object RepositoryProvider {

    val movieRepository: MovieRepository by lazy {
        RemoteMovieRepository()
    }

    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl()
    }

    val watchListRepository: WatchListRepository by lazy {
        WatchListRepositoryImpl(
            watchListDao = DatabaseProvider.getDatabase().watchListDao()
        )
    }

    val searchHistoryRepository: SearchHistoryRepository by lazy {
        SearchHistoryRepositoryImpl(
            dao = DatabaseProvider.getDatabase().searchHistoryDao()
        )
    }
}
