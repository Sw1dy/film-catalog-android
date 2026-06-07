package com.example.film_catalog_android.di

import com.example.film_catalog_android.domain.usecase.auth.LoginUseCase
import com.example.film_catalog_android.domain.usecase.auth.LogoutUseCase
import com.example.film_catalog_android.domain.usecase.auth.RegisterUseCase
import com.example.film_catalog_android.domain.usecase.history.AddSearchHistoryUseCase
import com.example.film_catalog_android.domain.usecase.history.ClearSearchHistoryUseCase
import com.example.film_catalog_android.domain.usecase.history.ObserveSearchHistoryUseCase
import com.example.film_catalog_android.domain.usecase.movie.AddMovieUseCase
import com.example.film_catalog_android.domain.usecase.movie.DeleteMovieUseCase
import com.example.film_catalog_android.domain.usecase.movie.GetMovieByIdUseCase
import com.example.film_catalog_android.domain.usecase.movie.GetMovieGenresUseCase
import com.example.film_catalog_android.domain.usecase.movie.GetMovieYearsUseCase
import com.example.film_catalog_android.domain.usecase.movie.GetMoviesUseCase
import com.example.film_catalog_android.domain.usecase.movie.ObserveMoviesUseCase
import com.example.film_catalog_android.domain.usecase.movie.SearchMoviesUseCase
import com.example.film_catalog_android.domain.usecase.movie.UpdateMovieUseCase
import com.example.film_catalog_android.domain.usecase.watchlist.ObserveWatchListIdsUseCase
import com.example.film_catalog_android.domain.usecase.watchlist.ObserveWatchListUseCase
import com.example.film_catalog_android.domain.usecase.watchlist.RemoveFromWatchListUseCase
import com.example.film_catalog_android.domain.usecase.watchlist.ToggleWatchListUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { LoginUseCase(authRepository = get()) }
    factory { RegisterUseCase(authRepository = get()) }
    factory { LogoutUseCase(authRepository = get()) }

    factory { GetMoviesUseCase(movieRepository = get()) }
    factory { ObserveMoviesUseCase(movieRepository = get()) }
    factory { GetMovieByIdUseCase(movieRepository = get()) }
    factory { SearchMoviesUseCase(movieRepository = get()) }
    factory { GetMovieGenresUseCase(movieRepository = get()) }
    factory { GetMovieYearsUseCase(movieRepository = get()) }
    factory { AddMovieUseCase(movieRepository = get()) }
    factory { UpdateMovieUseCase(movieRepository = get()) }
    factory { DeleteMovieUseCase(movieRepository = get()) }

    factory { ObserveWatchListUseCase(watchListRepository = get()) }
    factory { ObserveWatchListIdsUseCase(watchListRepository = get()) }
    factory { ToggleWatchListUseCase(watchListRepository = get()) }
    factory { RemoveFromWatchListUseCase(watchListRepository = get()) }

    factory { ObserveSearchHistoryUseCase(searchHistoryRepository = get()) }
    factory { AddSearchHistoryUseCase(searchHistoryRepository = get()) }
    factory { ClearSearchHistoryUseCase(searchHistoryRepository = get()) }
}
