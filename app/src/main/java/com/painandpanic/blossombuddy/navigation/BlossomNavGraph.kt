package com.painandpanic.blossombuddy.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun BlossomNavGraph(
    navController: NavHostController = rememberNavController(),
    // TODO: Add view models
) {
    NavHost(navController = navController, startDestination = Destination.Home.route) {
        composable(Destination.Home.route) {
            // TODO: Home screen
        }
        composable(Destination.Camera.route) {
            // TODO: Camera screen
        }
        composable(
            Destination.BlossomChat.route,
            arguments = listOf(navArgument(Destination.BlossomChat.argName) { type = NavType.IntType })
        ) {
            // TODO: Blossom chat screen
        }
        composable(Destination.Settings.route) {
            // TODO: Settings screen
        }
    }
}