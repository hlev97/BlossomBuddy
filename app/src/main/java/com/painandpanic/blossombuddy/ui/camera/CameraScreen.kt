package com.painandpanic.blossombuddy.ui.camera

import android.graphics.Bitmap
import android.graphics.Color
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.painandpanic.blossombuddy.util.events.EventEffect

@Composable
fun CameraScreen(
    onBack: () -> Unit,
    onPhotoCaptured: (LifecycleCameraController) -> Unit,
    onNavigateHome: () -> Unit,
    onPreviewedPhotoClicked: () -> Unit,
    state: CameraViewState
) {

    val context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember { LifecycleCameraController(context) }

    val lastCapturedPhoto = state.lastCapturePhoto

    EventEffect(event = state.savePhotoSuccess) {
        onNavigateHome()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                    .padding(16.dp),
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.Outlined.Close, contentDescription = null)
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onPhotoCaptured(cameraController) },
            ) {
                Image(imageVector = Icons.Outlined.CameraAlt, contentDescription = null)
            }
        }
    ) { paddingValues ->  
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                lifecycleOwner = lifecycleOwner,
                cameraController = cameraController,
            )

            AnimatedVisibility (
                visible = state.isPreviewDisplayed,
                modifier = Modifier.align(Alignment.BottomStart),
                enter = fadeIn() + slideInHorizontally { width -> -width },
                exit = fadeOut() + slideOutHorizontally { width -> width }
            ) {
                LastCapturedPhotoPreview(
                    modifier = Modifier.align(Alignment.BottomStart),
                    lastCapturedPhoto = lastCapturedPhoto ?: return@AnimatedVisibility,
                    onClick = onPreviewedPhotoClicked
                )
            }

        }
    }
}

@Composable
private fun CameraPreview(
    modifier: Modifier = Modifier,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->
            PreviewView(context).apply {
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                setBackgroundColor(Color.BLACK)
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_START
            }.also { previewView ->
                previewView.controller = cameraController
                cameraController.bindToLifecycle(lifecycleOwner)
            }
        }
    )
}

@Composable
fun LastCapturedPhotoPreview(
    modifier: Modifier = Modifier,
    lastCapturedPhoto: Bitmap,
    onClick: () -> Unit
) {
    val capturedPhoto: ImageBitmap = remember(lastCapturedPhoto.hashCode()) { lastCapturedPhoto.asImageBitmap() }
    var isPressed by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(targetValue = if (isPressed) 0.5f else 1f, label = "")
    Card(
        modifier = modifier
            .size(128.dp)
            .padding(16.dp)
            .clip(MaterialTheme.shapes.large)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(),
        shape = MaterialTheme.shapes.large,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                bitmap = capturedPhoto,
                contentDescription = "Last captured photo",
                contentScale = androidx.compose.ui.layout.ContentScale.Crop
            )

            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Save",
                tint = MaterialTheme.colorScheme.primaryContainer.copy(alpha = alpha),
            )
        }
    }
}

@Preview
@Composable
fun CameraScreen_Preview() {
    CameraScreen(
        onBack = {},
        onPhotoCaptured = {},
        onPreviewedPhotoClicked = {},
        onNavigateHome = {} ,
        state = CameraViewState()
    )
}