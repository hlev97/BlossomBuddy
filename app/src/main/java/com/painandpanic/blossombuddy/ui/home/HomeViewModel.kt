package com.painandpanic.blossombuddy.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.painandpanic.blossombuddy.domain.usecase.LoadHistoryUseCase
import com.painandpanic.blossombuddy.ui.model.HistoryUi
import com.painandpanic.blossombuddy.ui.model.PermissionUi
import com.painandpanic.blossombuddy.util.events.BasicStateEvent
import com.painandpanic.blossombuddy.util.events.StateEventWithContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val loadHistory: LoadHistoryUseCase
): ViewModel() {

    private val _uiStateStream = MutableStateFlow(HomeViewState())
    val uiStateStream = _uiStateStream.asStateFlow()
    private var uiState: HomeViewState
        get() = _uiStateStream.value
        set(newState) {
            _uiStateStream.update { newState }
        }

    fun load() {
        uiState = uiState.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.Default) {
            uiState = uiState.copy(
                isLoading = false,
                history = loadHistory()
            )
        }
    }

    fun onPhotoPicked(imageId: Long?) {
        uiState = try {
            if (imageId != null) {
                uiState.copy(photoPickedSuccess = StateEventWithContent.Triggered(imageId))
            } else {
                uiState.copy(photoPickedSuccess = StateEventWithContent.Triggered(null))
            }
        } catch (e: Exception) {
            uiState.copy(photoPickedFailure = BasicStateEvent.Triggered)
        }
    }

    fun onPhotoPickedFailureCaptured() {
        uiState = uiState.copy(photoPickedFailure = BasicStateEvent.Captured)
    }

    fun onShowPermissionResultSnackbarCaptured() {
        uiState = uiState.copy(showPermissionResultSnackbar = StateEventWithContent.Captured(""))
    }

    fun showPermissionRationaleDialog(permissionUi: PermissionUi) {
        uiState = when (permissionUi) {
            PermissionUi.Camera -> {
                uiState.copy(isCameraPermissionRationaleDialogShown = true)
            }

            PermissionUi.ReadMediaImages, PermissionUi.ReadMediaVisualUserSelected, PermissionUi.ReadExternalStorage -> {
                uiState.copy(isReadMediaPermissionRationaleDialogShown = true)
            }
        }
    }

    fun hidePermissionRationaleDialog(permissionUi: PermissionUi) {
        uiState = when (permissionUi) {
            PermissionUi.Camera -> {
                uiState.copy(isCameraPermissionRationaleDialogShown = false)
            }

            PermissionUi.ReadMediaImages, PermissionUi.ReadMediaVisualUserSelected, PermissionUi.ReadExternalStorage -> {
                uiState.copy(isReadMediaPermissionRationaleDialogShown = false)
            }
        }
    }

    fun onDismissPermissionRationaleDialog(permissionUi: PermissionUi) {
        uiState = when (permissionUi) {
            PermissionUi.Camera -> {
                uiState.copy(
                    isCameraPermissionRationaleDialogShown = false,
                    showPermissionResultSnackbar = StateEventWithContent.Triggered("Camera permission denied")
                )
            }

            PermissionUi.ReadMediaImages, PermissionUi.ReadMediaVisualUserSelected, PermissionUi.ReadExternalStorage -> {
                uiState.copy(
                    isReadMediaPermissionRationaleDialogShown = false,
                    showPermissionResultSnackbar = StateEventWithContent.Triggered("Read media permission denied")
                )
            }
        }
    }
}

data class HomeViewState(
    val isLoading: Boolean = false,
    val history: List<HistoryUi> = emptyList(),
    val photoPickedFailure: BasicStateEvent = BasicStateEvent.Captured,
    val photoPickedSuccess: StateEventWithContent<Long?> = StateEventWithContent.Captured(null),
    val showPermissionResultSnackbar: StateEventWithContent<String> = StateEventWithContent.Captured(""),
    val isCameraPermissionRationaleDialogShown: Boolean = false,
    val isReadMediaPermissionRationaleDialogShown: Boolean = false,
) {
    val isHistoryEmpty: Boolean
        get() = history.isEmpty()
}
