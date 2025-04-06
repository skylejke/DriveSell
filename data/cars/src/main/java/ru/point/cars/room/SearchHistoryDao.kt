package ru.point.cars.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.point.cars.model.SearchHistory

@Dao
interface SearchHistoryDao {
    @Insert
    suspend fun insertSearchHistoryItem(searchHistory: SearchHistory)

    @Query("SELECT * FROM searchHistory WHERE userId = :userId ORDER BY id DESC")
    fun getSearchHistory(userId: String): Flow<List<SearchHistory>>

    @Query("DELETE FROM searchHistory")
    suspend fun clearSearchHistory()
}