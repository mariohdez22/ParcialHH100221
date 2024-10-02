package com.example.parcialhh100221.Views

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable("location") {
            LocationScreen()
        }
        composable("media") {
            MediaUploadScreen()
        }
        composable("notifications") {
            NotificationPermissionScreen()
        }
        composable("camera") {
            CameraScreen()
        }
    }
}