package com.example.film_catalog_android.presentation.home

import com.example.film_catalog_android.domain.model.Movie

data class HomeUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
    val isAdmin: Boolean = false
)