package com.example.film_catalog_android.di

import com.example.film_catalog_android.data.repository.AuthRepositoryImpl
import com.example.film_catalog_android.data.repository.RemoteMovieRepository
import com.example.film_catalog_android.data.repository.SearchHistoryRepositoryImpl
import com.example.film_catalog_android.data.repository.WatchListRepositoryImpl
import com.example.film_catalog_android.domain.repository.AuthRepository
import com.example.film_catalog_android.domain.repository.MovieRepository
import com.example.film_catalog_android.domain.repository.SearchHistoryRepository
import com.example.film_catalog_android.domain.repository.WatchListRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<AuthRepository> {
        AuthRepositoryImpl(authApi = get())
    }
    single<MovieRepository> {
        RemoteMovieRepository(movieApi = get())
    }
    single<WatchListRepository> {
        WatchListRepositoryImpl(watchListDao = get())
    }
    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(dao = get())
    }
}
