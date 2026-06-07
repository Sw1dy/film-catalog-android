package com.example.film_catalog_android.di

import com.example.film_catalog_android.data.local.AppDatabase
import com.example.film_catalog_android.data.local.DatabaseProvider
import com.example.film_catalog_android.data.remote.AuthApi
import com.example.film_catalog_android.data.remote.MovieApi
import org.koin.dsl.module

val appModule = module {
    single<AppDatabase> { DatabaseProvider.getDatabase() }
    single { get<AppDatabase>().watchListDao() }
    single { get<AppDatabase>().searchHistoryDao() }
    single { AuthApi() }
    single { MovieApi() }
}
