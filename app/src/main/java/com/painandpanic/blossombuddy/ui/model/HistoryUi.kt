package com.painandpanic.blossombuddy.ui.model

import android.graphics.Bitmap

data class HistoryUi(
    val image: Bitmap,
    val imageID: Long,
    val label: String,
    val timestamp: String
)