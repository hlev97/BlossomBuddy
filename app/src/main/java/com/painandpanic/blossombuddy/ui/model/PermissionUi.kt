package com.painandpanic.blossombuddy.ui.model

import android.Manifest
import android.os.Build
import com.painandpanic.blossombuddy.R

sealed class PermissionUi(val title: Int, val rationale: Int) {
    data object Camera : PermissionUi(
        title = R.string.camera_permission_title,
        rationale = R.string.camera_permission_rationale
    )

    data object ReadMediaImages : PermissionUi(
        title = R.string.read_media_images_permission_title,
        rationale = R.string.read_media_images_permission_rationale
    )

    data object ReadMediaVisualUserSelected : PermissionUi(
        title = R.string.read_media_visual_user_selected_permission_title,
        rationale = R.string.read_media_visual_user_selected_permission_rationale
    )

    data object ReadExternalStorage : PermissionUi(
        title = R.string.read_external_storage_permission_title,
        rationale = R.string.read_external_storage_permission_rationale
    )

    companion object {
        fun String.permissionUi(): PermissionUi? = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> when (this) {
                Manifest.permission.CAMERA -> Camera
                Manifest.permission.READ_MEDIA_IMAGES -> ReadMediaImages
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED -> ReadMediaVisualUserSelected
                Manifest.permission.READ_EXTERNAL_STORAGE -> ReadExternalStorage
                else -> null
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> when (this) {
                Manifest.permission.CAMERA -> Camera
                Manifest.permission.READ_MEDIA_IMAGES -> ReadMediaImages
                else -> null
            }
            else -> when (this) {
                Manifest.permission.CAMERA -> Camera
                Manifest.permission.READ_EXTERNAL_STORAGE -> ReadExternalStorage
                else -> null
            }
        }
    }
}