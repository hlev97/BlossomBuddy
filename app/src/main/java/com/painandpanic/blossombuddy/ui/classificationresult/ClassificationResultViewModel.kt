package com.painandpanic.blossombuddy.ui.classificationresult

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.painandpanic.blossombuddy.domain.model.HistoryDomain
import com.painandpanic.blossombuddy.domain.usecase.ClassifyImageUseCase
import com.painandpanic.blossombuddy.domain.usecase.SaveToHistoryUseCase
import com.painandpanic.blossombuddy.navigation.Destination
import com.painandpanic.blossombuddy.util.events.BasicStateEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime

class ClassificationResultViewModel(
    private val classifyImage: ClassifyImageUseCase,
    private val saveToHistory: SaveToHistoryUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiStateStream = MutableStateFlow(ClassificationResultViewState())
    val uiStateStream = _uiStateStream.asStateFlow()

    private val imageId = savedStateHandle.get<Long>(Destination.ClassificationResult.argName) ?: throw IllegalArgumentException("Id is required")
    private var uiState: ClassificationResultViewState
        get() = _uiStateStream.value
        set(newState) {
            _uiStateStream.value = newState
        }

    private val vulkanMutex = Mutex()

    init { classify() }
    private fun classify() {
        uiState = uiState.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            delay(2000)
            uiState = try {
                val result = vulkanMutex.withLock {
                    classifyImage(imageId)
                }
                saveToHistory(HistoryDomain(imageId, result.third, LocalDateTime.now()))
                uiState.copy(
                    isLoading = false,
                    photoToClassify = result.first,
                    predictedLabel = result.third
                )
            } catch (e: Exception) {
                Log.e("error", e.stackTraceToString())
                uiState.copy(
                    isLoading = false,
                    classifyImageFailure = BasicStateEvent.Captured
                )
            }
        }
    }

    fun onClassifyImageFailureCaptured() {
        uiState = uiState.copy(classifyImageFailure = BasicStateEvent.Captured)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}

data class ClassificationResultViewState(
    val isLoading: Boolean = false,
    val photoToClassify: Bitmap? = null,
    val predictedLabel: String? = null,
    val classifyImageFailure: BasicStateEvent = BasicStateEvent.Captured,
) {
    val isPhotoPredicted
        get() = photoToClassify != null && predictedLabel != null
}