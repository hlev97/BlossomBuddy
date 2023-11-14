package com.painandpanic.blossombuddy.data.local.converters

import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeConverter {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime): String {
        return localDateTime.format(dateTimeFormatter)
    }

    @TypeConverter
    fun toLocalDateTime(localDateTimeString: String): LocalDateTime {
        return dateTimeFormatter.parse(localDateTimeString, LocalDateTime::from)
    }
}