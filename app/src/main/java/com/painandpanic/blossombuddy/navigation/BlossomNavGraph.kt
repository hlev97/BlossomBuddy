package com.painandpanic.blossombuddy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.painandpanic.blossombuddy.ui.camera.CameraScreen
import com.painandpanic.blossombuddy.ui.camera.CameraViewModel
import com.painandpanic.blossombuddy.ui.classificationresult.ClassificationResult
import com.painandpanic.blossombuddy.ui.classificationresult.ClassificationResultViewModel
import com.painandpanic.blossombuddy.ui.home.HomeScreen
import com.painandpanic.blossombuddy.ui.home.HomeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun BlossomNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navController, startDestination = Destination.Home.route) {
        composable(Destination.Home.route) {
            val homeViewModel = koinViewModel<HomeViewModel>()
            val homeUiState by homeViewModel.uiStateStream.collectAsState()
            HomeScreen(
                navigateToCamera = { navController.navigate(Destination.Camera.route) },
                navigateToClassifier = { imageId ->
                    navController.navigate(Destination.ClassificationResult.createRoute(imageId))
                },
                onPhotoPicked = homeViewModel::onPhotoPicked,
                onHistoryItemClicked = { imageId ->
                    navController.navigate(Destination.ClassificationResult.createRoute(imageId.toLong()))
                },
                onPhotoPickedFailureEventCaptured = homeViewModel::onPhotoPickedFailureCaptured,
                onShowCameraPermissionResultSnackBarCaptured = homeViewModel::onShowPermissionResultSnackbarCaptured,
                state = homeUiState,
                load = homeViewModel::load,
                showPermissionRationaleDialog = homeViewModel::showPermissionRationaleDialog,
                hidePermissionRationaleDialog = homeViewModel::hidePermissionRationaleDialog,
                onDismissPermissionRationaleDialog = homeViewModel::onDismissPermissionRationaleDialog
            )
        }
        composable(Destination.Camera.route) {
            val cameraViewModel = koinViewModel<CameraViewModel>()
            val cameraUiState by cameraViewModel.uiStateStream.collectAsState()
            CameraScreen(
                onBack = {
                    navController.popBackStack(Destination.Home.route, inclusive = true)
                    navController.navigate(Destination.Home.route)
                },
                onPhotoCaptured = cameraViewModel::onPhotoCaptured,
                onPreviewedPhotoClicked = cameraViewModel::onCapturePhotoPreviewed,
                state = cameraUiState
            )
        }
        composable(
            Destination.ClassificationResult.route,
            arguments = listOf(navArgument(Destination.ClassificationResult.argName) { type = NavType.LongType })
        ) {
            val classificationResultViewModel = koinViewModel<ClassificationResultViewModel>()
            val classificationResultUiState by classificationResultViewModel.uiStateStream.collectAsState()
            ClassificationResult(
                onBack = {
                    navController.popBackStack(Destination.Home.route, inclusive = true)
                    navController.navigate(Destination.Home.route)
                 },
                onClassifyImageFailureCaptured = classificationResultViewModel::onClassifyImageFailureCaptured,
                state = classificationResultUiState
            )
        }
    }
}