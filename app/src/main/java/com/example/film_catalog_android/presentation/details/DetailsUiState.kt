package com.example.film_catalog_android.presentation.details

import com.example.film_catalog_android.domain.model.Movie

data class DetailsUiState(
    val isLoading: Boolean = false,
    val movie: Movie? = null,
    val errorMessage: String? = null
)