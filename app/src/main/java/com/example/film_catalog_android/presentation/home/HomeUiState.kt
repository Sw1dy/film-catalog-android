package com.example.film_catalog_android.presentation.home

import com.example.film_catalog_android.domain.model.Movie

data class HomeUiState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val errorMessage: String? = null,
    val isAdmin: Boolean = false,

    val selectedGenre: String? = null,
    val selectedYear: Int? = null,
    val availableGenres: List<String> = emptyList(),
    val availableYears: List<Int> = emptyList(),
    val isFiltersLoading: Boolean = false,
    val filterErrorMessage: String? = null
)