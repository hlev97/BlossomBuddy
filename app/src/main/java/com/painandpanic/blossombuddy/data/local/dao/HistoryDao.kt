package com.painandpanic.blossombuddy.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.painandpanic.blossombuddy.data.local.entity.HistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insertHistoryItem(historyItem: HistoryEntity)

    @Query("SELECT * FROM history")
    fun getHistoryItems(): Flow<List<HistoryEntity>>

    @Query("SELECT * FROM history WHERE id = :id")
    fun getHistoryItem(id: Long): Flow<HistoryEntity>

    @Query("SELECT * FROM history ORDER BY id DESC LIMIT 1")
    fun getLastHistoryItem(): Flow<HistoryEntity>
}