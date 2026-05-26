package com.example.film_catalog_android.presentation.search

import com.example.film_catalog_android.domain.model.Movie

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<Movie> = emptyList(),
    val history: List<String> = emptyList(),
    val errorMessage: String? = null,
    val hasSearched: Boolean = false
)