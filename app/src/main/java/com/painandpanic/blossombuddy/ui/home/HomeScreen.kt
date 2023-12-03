package com.painandpanic.blossombuddy.ui.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.painandpanic.blossombuddy.ui.core.permissions.RequestMultiplePermissions
import com.painandpanic.blossombuddy.ui.model.PermissionUi
import com.painandpanic.blossombuddy.ui.theme.BlossomBuddyTheme
import com.painandpanic.blossombuddy.util.events.EventEffect
import com.painandpanic.blossombuddy.util.events.NavigationEventEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToCamera: () -> Unit,
    navigateToClassifier: (Long) -> Unit,
    onPhotoPicked: (Long) -> Unit,
    onHistoryItemClicked: (Int) -> Unit,
    onPhotoPickedFailureEventCaptured: () -> Unit,
    onShowCameraPermissionResultSnackBarCaptured: () -> Unit,
    showPermissionRationaleDialog: (PermissionUi) -> Unit,
    hidePermissionRationaleDialog: (PermissionUi) -> Unit,
    onDismissPermissionRationaleDialog: (PermissionUi) -> Unit,
    load: () -> Unit,
    state: HomeViewState,
) {

    RequestMultiplePermissions(
        state = state,
        showPermissionRationaleDialog = showPermissionRationaleDialog,
        onOkClick = hidePermissionRationaleDialog,
        onDismiss = onDismissPermissionRationaleDialog
    )

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { result ->
            result?.let {
                onPhotoPicked(it.lastPathSegment!!.toLong())
            }
        }
    )

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        load()
    }

    EventEffect(
        event = state.photoPickedFailure,
        onConsumed = onPhotoPickedFailureEventCaptured
    ) {
        snackbarHostState.showSnackbar(
            message = "Welcome to Blossom Buddy!"
        )
    }

    EventEffect(
        event = state.showPermissionResultSnackbar,
        onCaptured = onShowCameraPermissionResultSnackBarCaptured
    ) {
        snackbarHostState.showSnackbar(message = it)
    }

    NavigationEventEffect(event = state.photoPickedSuccess, onConsumed = { }) { pickedPhoto ->
        navigateToClassifier(pickedPhoto!!)
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(
                    text = "History",
                    style = MaterialTheme.typography.headlineMedium
                ) }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                tonalElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 16.dp),
                ) {
                    Text(
                        text = "Take a photo or select one from your gallery",
                        modifier = Modifier.weight(4f)
                    )
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    FilledIconButton(
                        onClick = {
                            navigateToCamera()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(imageVector = Icons.Outlined.CameraAlt, contentDescription = null)
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    OutlinedIconButton(
                        onClick = {
                            singlePhotoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Image(imageVector = Icons.Outlined.Image, contentDescription = null)
                    }
                }
            }
        }
    ) { paddingValues ->
        Box {
            when {
                state.isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                state.isHistoryEmpty -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Welcome to Blossom Buddy!",
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        items(state.history) { item ->
                            ListItem(
                                headlineContent = { Text(
                                    text = item.label,
                                    style = MaterialTheme.typography.titleLarge
                                ) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(
                                        onClick = { onHistoryItemClicked(state.history.indexOf(item)+1) }
                                    )
                                    .padding(8.dp),
                                leadingContent = {
                                    Image(
                                        bitmap = item.image.asImageBitmap(),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .clip(CircleShape)
                                            .size(64.dp),
                                        contentScale = ContentScale.Crop
                                    )
                                },
                                supportingContent = {
                                    Text(
                                        text = item.timestamp,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen_Preview() {
    BlossomBuddyTheme {
        HomeScreen(
            navigateToCamera = { /*TODO*/ },
            navigateToClassifier = {},
            onPhotoPicked = { /*TODO*/ },
            onHistoryItemClicked = { /*TODO*/ },
            onPhotoPickedFailureEventCaptured = { /*TODO*/ },
            onShowCameraPermissionResultSnackBarCaptured = { /*TODO*/ },
            state = HomeViewState(),
            load = {},
            showPermissionRationaleDialog = { /*TODO*/ },
            hidePermissionRationaleDialog = { /*TODO*/ },
            onDismissPermissionRationaleDialog = { /*TODO*/ },
        )
    }
}