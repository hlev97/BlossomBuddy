package com.painandpanic.blossombuddy.domain.usecase

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.painandpanic.blossombuddy.data.local.dao.HistoryDao
import kotlinx.coroutines.flow.first

class LoadImageFromGalleryUseCase(
    private val context: Context,
    private val dao: HistoryDao
) {

    suspend operator fun invoke(id: Long): Bitmap {
        val imageId = dao.getHistoryItem(id).first().imageId
        val contentUri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val uri: Uri = ContentUris.withAppendedId(contentUri, imageId)
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(context.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }

    }
}