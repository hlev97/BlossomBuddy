package com.painandpanic.blossombuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.painandpanic.blossombuddy.ui.theme.BlossomBuddyTheme
import android.Manifest
import com.painandpanic.blossombuddy.util.isCameraPermissionGranted
import com.painandpanic.blossombuddy.util.shouldShowRequestPermissionRationale

class MainActivity : ComponentActivity() {

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // TODO: Show snackbar that permission is granted
        } else {
            // TODO: Show snackbar that permission is not granted
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BlossomBuddyTheme {

            }
        }
        requestCameraPermission()
    }


    private fun requestCameraPermission() {
        when {
            isCameraPermissionGranted() -> Unit
            shouldShowRequestPermissionRationale() -> {
                // TODO: Show rationale
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }
}

