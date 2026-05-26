package com.example.film_catalog_android.presentation.profile

import com.example.film_catalog_android.domain.model.Movie

data class ProfileUiState(
    val firstName: String = "Никита",
    val lastName: String = "Породин",
    val watchList: List<Movie> = emptyList()
)