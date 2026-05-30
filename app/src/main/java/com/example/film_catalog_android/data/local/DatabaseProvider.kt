package com.example.film_catalog_android.data.local

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    private var database: AppDatabase? = null

    fun init(context: Context) {
        if (database == null) {
            database = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "film_catalog_database"
            )
                .fallbackToDestructiveMigration()
                .build()
        }
    }

    fun getDatabase(): AppDatabase {
        return database ?: error("DatabaseProvider is not initialized")
    }
}
