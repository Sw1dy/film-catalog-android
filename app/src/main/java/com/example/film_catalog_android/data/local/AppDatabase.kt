package com.example.film_catalog_android.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.film_catalog_android.data.local.dao.SearchHistoryDao
import com.example.film_catalog_android.data.local.dao.WatchListDao
import com.example.film_catalog_android.data.local.entity.SearchHistoryEntity
import com.example.film_catalog_android.data.local.entity.WatchListEntity

@Database(
    entities = [
        SearchHistoryEntity::class,
        WatchListEntity::class
    ],
    version = 5,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao

    abstract fun watchListDao(): WatchListDao
}
