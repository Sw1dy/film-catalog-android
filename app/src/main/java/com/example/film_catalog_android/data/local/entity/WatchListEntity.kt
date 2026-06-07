package com.example.film_catalog_android.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "watchlist",
    primaryKeys = ["userId", "movieId"]
)
data class WatchListEntity(
    val userId: Long,
    val movieId: Long,
    val title: String,
    val description: String,
    val year: Int,
    val genre: String,
    val rating: Double,
    val imageUrl: String
)
