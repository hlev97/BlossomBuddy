package com.painandpanic.blossombuddy.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.painandpanic.blossombuddy.data.local.converters.LocalDateTimeConverter
import com.painandpanic.blossombuddy.data.local.converters.UriConverter
import com.painandpanic.blossombuddy.data.local.dao.HistoryDao
import com.painandpanic.blossombuddy.data.local.entity.HistoryEntity


@Database(entities = [HistoryEntity::class], version = 1,)
@TypeConverters(UriConverter::class, LocalDateTimeConverter::class)
abstract class HistoryDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}