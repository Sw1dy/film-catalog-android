package com.example.film_catalog_android.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.film_catalog_android.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history ORDER BY createdAt DESC LIMIT 10")
    fun observeHistory(): Flow<List<SearchHistoryEntity>>

    @Query("DELETE FROM search_history WHERE movieId = :movieId")
    suspend fun deleteByMovieId(movieId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SearchHistoryEntity)

    @Query("""
        DELETE FROM search_history 
        WHERE id NOT IN (
            SELECT id FROM search_history 
            ORDER BY createdAt DESC 
            LIMIT 10
        )
    """)
    suspend fun deleteOldItems()

    @Query("DELETE FROM search_history")
    suspend fun clearHistory()
}