package com.example.film_catalog_android.data.remote.dto

data class CreateMovieRequest(
    val title: String,
    val description: String,
    val year: Int,
    val genre: String,
    val rating: Double,
    val imageUrl: String
)