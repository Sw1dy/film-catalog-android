package com.example.film_catalog_android.presentation.profile

import com.example.film_catalog_android.domain.model.Movie

data class ProfileUiState(
    val firstName: String = "",
    val lastName: String = "",
    val watchList: List<Movie> = emptyList(),

    val selectedGenre: String? = null,
    val selectedYear: Int? = null,
    val availableGenres: List<String> = emptyList(),
    val availableYears: List<Int> = emptyList()
)
