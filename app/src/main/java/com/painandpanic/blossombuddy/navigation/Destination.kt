package com.painandpanic.blossombuddy.navigation

sealed class Destination(val route: String) {
    data object Home : Destination(route = "home")
    data object Camera : Destination(route = "camera")
    data object BlossomChat : Destination(route = "blossom_chat/{blossomId}") {
        const val argName = "blossomId"
        fun createRoute(blossomId: String) = "blossom_chat/$blossomId"
    }
    data object ClassificationResult : Destination(route = "classification_result/{imageId}") {
        const val argName = "imageId"
        fun createRoute(imageId: Long) = "classification_result/$imageId"
    }
    data object Settings : Destination(route = "settings")
}