package com.example.film_catalog_android.presentation.search

import com.example.film_catalog_android.domain.model.Movie
import com.example.film_catalog_android.domain.model.SearchHistoryItem

data class SearchUiState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<Movie> = emptyList(),
    val history: List<SearchHistoryItem> = emptyList(),
    val errorMessage: String? = null,
    val hasSearched: Boolean = false
)