package com.painandpanic.blossombuddy.domain.model

import com.painandpanic.blossombuddy.data.local.entity.HistoryEntity
import java.time.LocalDateTime

data class HistoryDomain(
    val id: Long = 0,
    val imageId: Long,
    val predictedLabel: String,
    val timestamp: LocalDateTime,
)

fun HistoryDomain.toEntity() = HistoryEntity(
    id = id,
    predictedLabel = predictedLabel,
    imageId = imageId,
    timestamp = timestamp,
)

fun HistoryEntity.toDomain() = HistoryDomain(
    id = id,
    imageId = imageId,
    predictedLabel = predictedLabel,
    timestamp = timestamp,
)