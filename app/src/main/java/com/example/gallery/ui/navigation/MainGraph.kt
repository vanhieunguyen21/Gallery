package com.example.gallery.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.gallery.ui.screen.HomeScreen
import com.example.gallery.ui.screen.BucketScreen

@Composable
fun MainGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route, modifier = modifier) {
        composable(Screen.HomeScreen.route) { HomeScreen(navController = navController) }

        composable(
            Screen.BucketScreen.route,
            arguments = listOf(
                navArgument("bucket") {
                    type = NavType.StringType
                }
            )
        ) { BucketScreen(navController = navController) }
    }
}