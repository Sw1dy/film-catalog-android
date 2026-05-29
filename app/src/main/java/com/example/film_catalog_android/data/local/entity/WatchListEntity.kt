package com.example.film_catalog_android.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class WatchListEntity(
    @PrimaryKey
    val movieId: Long,
    val addedAt: Long
)