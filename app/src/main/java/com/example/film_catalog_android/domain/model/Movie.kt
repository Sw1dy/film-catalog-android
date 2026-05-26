package com.example.film_catalog_android.domain.model

data class Movie(
    val id: Long,
    val title: String,
    val description: String,
    val year: Int,
    val genre: String,
    val rating: Double,
    val imageUrl: String,
    val isInWatchlist: Boolean = false
) {
    fun isValid(): Boolean {
        return title.isNotBlank() &&
                description.isNotBlank() &&
                year > 1888 &&
                genre.isNotBlank() &&
                rating in 0.0..10.0
    }
}