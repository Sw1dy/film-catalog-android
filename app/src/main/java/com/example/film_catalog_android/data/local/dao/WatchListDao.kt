package com.example.film_catalog_android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.film_catalog_android.data.local.entity.WatchListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchListDao {

    @Query("SELECT movieId FROM watchlist WHERE userId = :userId")
    fun observeWatchListIds(userId: Long): Flow<List<Long>>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE userId = :userId AND movieId = :movieId)")
    suspend fun isMovieInWatchList(userId: Long, movieId: Long): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WatchListEntity)

    @Query("DELETE FROM watchlist WHERE userId = :userId AND movieId = :movieId")
    suspend fun delete(userId: Long, movieId: Long)

    @Query("DELETE FROM watchlist WHERE userId = :userId")
    suspend fun clearForUser(userId: Long)
}
