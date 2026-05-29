package com.example.film_catalog_android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.film_catalog_android.data.local.entity.WatchListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListDao {

    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun observeWatchList(): Flow<List<WatchListEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE movieId = :movieId)")
    suspend fun isInWatchList(movieId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WatchListEntity)

    @Query("DELETE FROM watchlist WHERE movieId = :movieId")
    suspend fun delete(movieId: Long)

    @Query("DELETE FROM watchlist")
    suspend fun clear()
}