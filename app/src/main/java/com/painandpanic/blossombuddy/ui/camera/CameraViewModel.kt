package com.painandpanic.blossombuddy.ui.camera

import android.graphics.Bitmap
import androidx.camera.view.LifecycleCameraController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.painandpanic.blossombuddy.domain.usecase.SavePhotoToGalleryUseCase
import com.painandpanic.blossombuddy.domain.usecase.TakePhotoUseCase
import com.painandpanic.blossombuddy.util.events.BasicStateEvent
import com.painandpanic.blossombuddy.util.events.StateEventWithContent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CameraViewModel(
    private val savePhotoToGallery: SavePhotoToGalleryUseCase,
    private val takePhoto: TakePhotoUseCase
) : ViewModel() {

    private val _uiStateStream = MutableStateFlow(CameraViewState())
    val uiStateStream = _uiStateStream.asStateFlow()

    private var uiState: CameraViewState
        get() = _uiStateStream.value
        set(newState) {
            _uiStateStream.update { newState }
        }

    fun onPhotoCaptured(cameraController: LifecycleCameraController) {
        takePhoto(
            cameraController = cameraController,
            onPhotoCapturedSuccess = { bitmap ->
                uiState.lastCapturePhoto?.recycle()
                uiState = uiState.copy(
                    isPreviewDisplayed = true,
                    lastCapturePhoto = bitmap
                )
            },
            onPhotoCapturedFailure = { uiState = uiState.copy(savePhotoFailure = BasicStateEvent.Triggered) }
        )
    }

    fun onCapturePhotoPreviewed() {
        try {
            viewModelScope.launch {
                savePhotoToGallery(uiState.lastCapturePhoto!!)
                uiState.lastCapturePhoto?.recycle()
                uiState = uiState.copy(isPreviewDisplayed = false)
                delay(100)
                uiState = uiState.copy(
                    lastCapturePhoto = null,
                    savePhotoSuccess = BasicStateEvent.Triggered
                )
            }
        } catch (e: Exception) {
            uiState = uiState.copy(
                lastCapturePhoto = null,
                savePhotoFailure = BasicStateEvent.Triggered
            )
        }
    }

    fun onPhotoSavedSuccesfully() {
        uiState = uiState.copy(savePhotoSuccess = BasicStateEvent.Captured)
    }
}

data class CameraViewState(
    val lastCapturePhoto: Bitmap? = null,
    val isPreviewDisplayed: Boolean = false,
    val savePhotoSuccess: BasicStateEvent = BasicStateEvent.Captured,
    val savePhotoFailure: BasicStateEvent = BasicStateEvent.Captured,
)