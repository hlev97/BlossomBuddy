package com.painandpanic.blossombuddy.domain.usecase

import com.painandpanic.blossombuddy.data.local.dao.HistoryDao
import com.painandpanic.blossombuddy.domain.model.HistoryDomain
import com.painandpanic.blossombuddy.domain.model.toEntity

class SaveToHistoryUseCase(
    private val dao: HistoryDao
) {
    suspend operator fun invoke(history: HistoryDomain) {
        dao.insertHistoryItem(history.toEntity())
    }
}