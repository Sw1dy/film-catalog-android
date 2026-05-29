package com.example.film_catalog_android.presentation.admin

import com.example.film_catalog_android.domain.model.Movie

data class ManageMoviesUiState(
    val query: String = "",
    val movies: List<Movie> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)