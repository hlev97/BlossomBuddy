package com.painandpanic.blossombuddy.domain.usecase

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import com.painandpanic.blossombuddy.util.rotateBitmap
import java.util.concurrent.Executor

class TakePhotoUseCase(
    private val context: Context
) {
    operator fun invoke(
        cameraController: LifecycleCameraController,
        onPhotoCapturedSuccess: (Bitmap) -> Unit,
        onPhotoCapturedFailure: (e: ImageCaptureException) -> Unit
    ) {
        val mainExecutor: Executor = ContextCompat.getMainExecutor(context)

        cameraController.takePicture(mainExecutor, object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                val correctedBitmap: Bitmap = image
                    .toBitmap()
                    .rotateBitmap(image.imageInfo.rotationDegrees)

                onPhotoCapturedSuccess(correctedBitmap)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                onPhotoCapturedFailure(exception)
            }
        })
    }
}