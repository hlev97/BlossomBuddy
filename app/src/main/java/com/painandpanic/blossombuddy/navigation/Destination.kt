package com.painandpanic.blossombuddy.navigation

sealed class Destination(val route: String) {
    data object Home : Destination(route = "home")
    data object Camera : Destination(route = "camera")

    data object ClassificationResult : Destination(route = "classification_result/{imageId}") {
        const val argName = "imageId"
        fun createRoute(imageId: Long) = "classification_result/$imageId"
    }

    data object HistoryItem : Destination(route = "history_item/{id}") {
        const val argName = "id"
        fun createRoute(id: Int) = "history_item/$id"
    }
}