package com.painandpanic.blossombuddy.domain.usecase

import android.content.ContentUris
import android.content.Context
import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.painandpanic.blossombuddy.R
import com.painandpanic.blossombuddy.util.assetFilePath
import org.pytorch.Device
import org.pytorch.IValue
import org.pytorch.LiteModuleLoader
import org.pytorch.Module
import org.pytorch.torchvision.TensorImageUtils

class ClassifyImageUseCase(
    private val context: Context,
) {

    operator fun invoke(imageId: Long): Triple<Bitmap,Int,String> {
        val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val imageUri: Uri = ContentUris.withAppendedId(contentUri, imageId)

        val classifier: Module = LiteModuleLoader.loadModuleFromAsset(context.assets, ASSET_NAME, Device.VULKAN)
        val image = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.RGBA_F16, true)
        }


        val tensorImage = TensorImageUtils.bitmapToFloat32Tensor(
            image,
            TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
            TensorImageUtils.TORCHVISION_NORM_STD_RGB
        )

        val outputTensor = classifier
            .forward(IValue.from(tensorImage))
            .toTensor()

        val scores = outputTensor?.dataAsFloatArray
        val maxScoreIdx = scores?.indices?.maxByOrNull { scores[it] } ?: -1
        val label = context.resources.getStringArray(R.array.flower_names)[maxScoreIdx]
        return Triple(image, maxScoreIdx, label)
    }

    operator fun invoke(image: Bitmap): Triple<Bitmap,Int,String> {
        val classifier: Module = LiteModuleLoader.load(assetFilePath(context, ASSET_NAME))
        val tensorImage = TensorImageUtils.bitmapToFloat32Tensor(
            image,
            TensorImageUtils.TORCHVISION_NORM_MEAN_RGB,
            TensorImageUtils.TORCHVISION_NORM_STD_RGB
        )

        val outputTensor = classifier
            .forward(IValue.from(tensorImage))
            .toTensor()

        val scores = outputTensor?.dataAsFloatArray
        val maxScoreIdx = scores?.indices?.maxByOrNull { scores[it] } ?: -1
        val label = context.resources.getStringArray(R.array.flower_names)[maxScoreIdx]
        return Triple(image, maxScoreIdx, label)
    }

    companion object {
        const val ASSET_NAME = "model.ptl"
    }
}
