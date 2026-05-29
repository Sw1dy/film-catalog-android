package com.example.film_catalog_android.presentation.admin

data class MovieFormUiState(
    val imageUrl: String = "",
    val title: String = "",
    val year: String = "",
    val genre: String = "",
    val rating: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)