package com.example.film_catalog_android.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "watchlist",
    primaryKeys = ["userId", "movieId"]
)
data class WatchListEntity(
    val userId: Long,
    val movieId: Long
)
