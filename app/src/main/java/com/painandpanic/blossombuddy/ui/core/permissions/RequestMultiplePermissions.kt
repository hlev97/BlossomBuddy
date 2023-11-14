package com.painandpanic.blossombuddy.ui.core.permissions

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import com.painandpanic.blossombuddy.ui.home.HomeViewState
import com.painandpanic.blossombuddy.ui.model.PermissionUi
import com.painandpanic.blossombuddy.ui.model.PermissionUi.Companion.permissionUi
import com.painandpanic.blossombuddy.util.isPermissionDenied
import com.painandpanic.blossombuddy.util.isReadMediaPermissionPartiallyGranted
import com.painandpanic.blossombuddy.util.openAppSettings

@Composable
fun RequestMultiplePermissions(
    state: HomeViewState,
    showPermissionRationaleDialog: (PermissionUi) -> Unit,
    onOkClick: (PermissionUi) -> Unit,
    onDismiss: (PermissionUi) -> Unit,
) {
    val context = LocalContext.current
    val activity = context as Activity

    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES,
            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_MEDIA_IMAGES,
        )
    } else {
        arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
    }

    val requestMultiplePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.forEach { (permission, isGranted) ->
            val permissionUi = permission.permissionUi()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                if (permission == Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) {
                    val isPartiallyGranted = context.isReadMediaPermissionPartiallyGranted()
                    if (!isPartiallyGranted) {
                        if (shouldShowRequestPermissionRationale(activity, permission)) {
                            permissionUi?.let(showPermissionRationaleDialog)
                        }
                    }
                }
            } else {
                if (!isGranted) {
                    if (shouldShowRequestPermissionRationale(activity, permission)) {
                        permissionUi?.let(showPermissionRationaleDialog)
                    }
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        permissionsToRequest.forEach {  permissionsToRequest ->
            if(context.isPermissionDenied(permissionsToRequest)) {
                if (!shouldShowRequestPermissionRationale(activity, permissionsToRequest)) {
                    permissionsToRequest.permissionUi()?.let(showPermissionRationaleDialog)
                } else {
                    requestMultiplePermissionLauncher.launch(arrayOf(permissionsToRequest))
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                when {
                    state.isCameraPermissionRationaleDialogShown -> PermissionRationaleDialog(
                        permissionName = stringResource(id = PermissionUi.Camera.title),
                        rationale = stringResource(id = PermissionUi.Camera.rationale),
                        isPermanentlyDeclined = shouldShowRequestPermissionRationale(activity,Manifest.permission.CAMERA),
                        onDismiss = { onDismiss(PermissionUi.Camera) },
                        onOkClick = {
                            requestMultiplePermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                            onOkClick(PermissionUi.Camera)
                        },
                        onGoToAppSettingsClick = { context.openAppSettings() }
                    )
                    state.isReadMediaPermissionRationaleDialogShown -> PermissionRationaleDialog(
                        permissionName = stringResource(id = PermissionUi.ReadMediaVisualUserSelected.title),
                        rationale = stringResource(id = PermissionUi.ReadMediaVisualUserSelected.rationale),
                        isPermanentlyDeclined = shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED),
                        onDismiss = { onDismiss(PermissionUi.ReadMediaImages) },
                        onOkClick = {
                            requestMultiplePermissionLauncher.launch(arrayOf(
                                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
                                    Manifest.permission.READ_MEDIA_IMAGES
                            ))
                            onOkClick(PermissionUi.ReadMediaImages)
                        },
                        onGoToAppSettingsClick = { context.openAppSettings() }
                    )
                }
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                when {
                    state.isCameraPermissionRationaleDialogShown -> PermissionRationaleDialog(
                        permissionName = stringResource(id = PermissionUi.Camera.title),
                        rationale = stringResource(id = PermissionUi.Camera.rationale),
                        isPermanentlyDeclined = shouldShowRequestPermissionRationale(activity,Manifest.permission.CAMERA),
                        onDismiss = { onDismiss(PermissionUi.Camera) },
                        onOkClick = {
                            requestMultiplePermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                            onOkClick(PermissionUi.Camera)
                        },
                        onGoToAppSettingsClick = { context.openAppSettings() }
                    )
                    state.isReadMediaPermissionRationaleDialogShown -> PermissionRationaleDialog(
                        permissionName = stringResource(id = PermissionUi.ReadMediaImages.title),
                        rationale = stringResource(id = PermissionUi.ReadMediaImages.rationale),
                        isPermanentlyDeclined = shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_MEDIA_IMAGES),
                        onDismiss = { onDismiss(PermissionUi.ReadMediaImages) },
                        onOkClick = {
                            requestMultiplePermissionLauncher.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
                            onOkClick(PermissionUi.ReadMediaImages)
                        },
                        onGoToAppSettingsClick = { context.openAppSettings() }
                    )
                }
            }
            else -> {
                when {
                    state.isCameraPermissionRationaleDialogShown -> PermissionRationaleDialog(
                        permissionName = stringResource(id = PermissionUi.Camera.title),
                        rationale = stringResource(id = PermissionUi.Camera.rationale),
                        isPermanentlyDeclined = shouldShowRequestPermissionRationale(activity,Manifest.permission.CAMERA),
                        onDismiss = { onDismiss(PermissionUi.Camera) },
                        onOkClick = {
                            requestMultiplePermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
                            onOkClick(PermissionUi.Camera)
                        },
                        onGoToAppSettingsClick = { context.openAppSettings() }
                    )
                    state.isReadMediaPermissionRationaleDialogShown -> PermissionRationaleDialog(
                        permissionName = stringResource(id = PermissionUi.ReadExternalStorage.title),
                        rationale = stringResource(id = PermissionUi.ReadExternalStorage.rationale),
                        isPermanentlyDeclined = shouldShowRequestPermissionRationale(activity,Manifest.permission.READ_EXTERNAL_STORAGE),
                        onDismiss = { onDismiss(PermissionUi.ReadExternalStorage) },
                        onOkClick = {
                            requestMultiplePermissionLauncher.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                            onOkClick(PermissionUi.ReadExternalStorage)
                        },
                        onGoToAppSettingsClick = { context.openAppSettings() }
                    )
                }
            }
        }
    }
}