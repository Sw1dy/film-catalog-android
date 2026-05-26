package com.example.film_catalog_android.domain.model

data class WatchListItem(
    val id: Long,
    val userId: String,
    val movieId: Long,
    val addedAt: Long
)