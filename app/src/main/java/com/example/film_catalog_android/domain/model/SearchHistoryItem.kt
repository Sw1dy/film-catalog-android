package com.example.film_catalog_android.domain.model

data class SearchHistoryItem(
    val id: Long,
    val userId: String,
    val query: String,
    val createdAt: Long
)