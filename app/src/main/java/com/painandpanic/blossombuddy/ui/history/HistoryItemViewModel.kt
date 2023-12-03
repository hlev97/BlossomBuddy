package com.painandpanic.blossombuddy.ui.history

import android.graphics.Bitmap
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.painandpanic.blossombuddy.domain.usecase.LoadHistoryUseCase
import com.painandpanic.blossombuddy.navigation.Destination
import com.painandpanic.blossombuddy.util.events.BasicStateEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryItemViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val loadHistory: LoadHistoryUseCase
) : ViewModel() {
    fun onLoadFailureCaptured() {
        uiState = uiState.copy(loadFailure = BasicStateEvent.Captured)
    }

    private val _uiStateStream = MutableStateFlow(HistoryItemViewState())
    val uiStateStream = _uiStateStream.asStateFlow()

    private val id = savedStateHandle.get<Int>(Destination.HistoryItem.argName) ?: throw IllegalArgumentException("Id is required")
    private var uiState: HistoryItemViewState
        get() = _uiStateStream.value
        set(newState) {
            _uiStateStream.value = newState
        }

    init {
        uiState = uiState.copy(isLoading = true)
        try {
            viewModelScope.launch(Dispatchers.IO) {
                val result = loadHistory(id.toLong())
                uiState = uiState.copy(
                    isLoading = false,
                    classifiedPhoto = result.image,
                    predictedLabel = result.label
                )
            }
        } catch (e: Exception) {
            uiState = uiState.copy(
                isLoading = false,
                loadFailure = BasicStateEvent.Captured
            )
        }
    }
}

data class HistoryItemViewState(
    val isLoading: Boolean = false,
    val classifiedPhoto: Bitmap? = null,
    val predictedLabel: String? = null,
    val loadFailure: BasicStateEvent = BasicStateEvent.Captured
) {

    val isLoaded: Boolean
        get() = classifiedPhoto != null && predictedLabel != null
    companion object {
        val Empty = HistoryItemViewState()
    }
}