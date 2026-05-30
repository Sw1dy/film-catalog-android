package com.example.film_catalog_android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.film_catalog_android.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history WHERE userId = :userId ORDER BY createdAt DESC LIMIT 10")
    fun observeHistory(userId: Long): Flow<List<SearchHistoryEntity>>

    @Query("DELETE FROM search_history WHERE userId = :userId AND movieId = :movieId")
    suspend fun deleteByMovieId(userId: Long, movieId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SearchHistoryEntity)

    @Query("""
        DELETE FROM search_history 
        WHERE userId = :userId
        AND id NOT IN (
            SELECT id FROM search_history 
            WHERE userId = :userId
            ORDER BY createdAt DESC 
            LIMIT 10
        )
    """)
    suspend fun deleteOldItems(userId: Long)

    @Query("DELETE FROM search_history WHERE userId = :userId")
    suspend fun clearHistory(userId: Long)
}
