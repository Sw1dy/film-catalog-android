package com.example.film_catalog_android.di

import com.example.film_catalog_android.presentation.admin.AddMovieViewModel
import com.example.film_catalog_android.presentation.admin.EditMovieViewModel
import com.example.film_catalog_android.presentation.admin.ManageMoviesViewModel
import com.example.film_catalog_android.presentation.auth.AuthStartViewModel
import com.example.film_catalog_android.presentation.auth.AuthViewModel
import com.example.film_catalog_android.presentation.details.DetailsViewModel
import com.example.film_catalog_android.presentation.home.HomeViewModel
import com.example.film_catalog_android.presentation.profile.ProfileViewModel
import com.example.film_catalog_android.presentation.profile.settings.SettingsViewModel
import com.example.film_catalog_android.presentation.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        AuthViewModel(
            loginUseCase = get(),
            registerUseCase = get()
        )
    }
    viewModel { AuthStartViewModel() }
    viewModel {
        HomeViewModel(
            getMoviesUseCase = get(),
            observeMoviesUseCase = get(),
            getMovieGenresUseCase = get(),
            getMovieYearsUseCase = get(),
            observeWatchListIdsUseCase = get(),
            toggleWatchListUseCase = get()
        )
    }
    viewModel {
        SearchViewModel(
            savedStateHandle = get(),
            searchMoviesUseCase = get(),
            observeSearchHistoryUseCase = get(),
            addSearchHistoryUseCase = get(),
            clearSearchHistoryUseCase = get()
        )
    }
    viewModel {
        DetailsViewModel(
            savedStateHandle = get(),
            getMovieByIdUseCase = get(),
            observeWatchListIdsUseCase = get(),
            toggleWatchListUseCase = get()
        )
    }
    viewModel {
        ProfileViewModel(
            observeWatchListUseCase = get(),
            removeFromWatchListUseCase = get()
        )
    }
    viewModel {
        SettingsViewModel(
            logoutUseCase = get()
        )
    }
    viewModel {
        AddMovieViewModel(
            addMovieUseCase = get()
        )
    }
    viewModel {
        EditMovieViewModel(
            savedStateHandle = get(),
            getMovieByIdUseCase = get(),
            updateMovieUseCase = get()
        )
    }
    viewModel {
        ManageMoviesViewModel(
            getMoviesUseCase = get(),
            observeMoviesUseCase = get(),
            deleteMovieUseCase = get()
        )
    }
}
