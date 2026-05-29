package com.example.film_catalog_android.data.remote.dto

data class MovieDto(
    val id: Long,
    val title: String,
    val description: String,
    val year: Int,
    val genre: String,
    val rating: Double,
    val imageUrl: String
)