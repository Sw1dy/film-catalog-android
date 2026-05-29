package com.example.film_catalog_android.domain.model

data class SearchHistoryItem(
    val id: Long,
    val movieId: Long,
    val title: String,
    val createdAt: Long
)