package com.painandpanic.blossombuddy.navigation

sealed class Destination(val route: String) {
    data object Home : Destination(route = "home")
    data object Camera : Destination(route = "camera")
    data object BlossomChat : Destination(route = "blossom_chat") {
        val argName = "blossomId"
        fun createRoute(blossomId: String) = "blossom_chat/$blossomId"
    }
    data object Settings : Destination(route = "settings")
}