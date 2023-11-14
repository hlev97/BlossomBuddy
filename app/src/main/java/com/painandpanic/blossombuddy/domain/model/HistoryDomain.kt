package com.painandpanic.blossombuddy.domain.model

import android.net.Uri
import com.painandpanic.blossombuddy.data.local.entity.HistoryEntity
import java.time.LocalDateTime

data class HistoryDomain(
    val id: Long,
    val predictedLabel: String,
    val timestamp: LocalDateTime,
)

fun HistoryDomain.toEntity() = HistoryEntity(
    id = id,
    predictedLabel = predictedLabel,
    timestamp = timestamp,
)

fun HistoryEntity.toDomain() = HistoryDomain(
    id = id,
    predictedLabel = predictedLabel,
    timestamp = timestamp,
)