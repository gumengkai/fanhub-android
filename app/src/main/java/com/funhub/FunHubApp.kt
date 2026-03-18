package com.funhub

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.funhub.ui.clips.ClipScreen
import com.funhub.ui.images.ImageDetailScreen
import com.funhub.ui.main.MainScreen
import com.funhub.ui.search.SearchScreen
import com.funhub.ui.settings.EnhancedSettingsScreen
import com.funhub.ui.videos.VideoPlayerScreen

@Composable
fun FunHubApp() {
    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = "main"
        ) {
            composable("main") {
                MainScreen(navController = navController)
            }

            composable(
                route = "video/{videoId}",
                arguments = listOf(navArgument("videoId") { type = NavType.StringType })
            ) { backStackEntry ->
                val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
                VideoPlayerScreen(
                    videoId = videoId,
                    onBackClick = { navController.navigateUp() },
                    onClipClick = { navController.navigate("clip?videoId=$it") }
                )
            }

            composable(
                route = "image/{imageId}",
                arguments = listOf(navArgument("imageId") { type = NavType.StringType })
            ) { backStackEntry ->
                val imageId = backStackEntry.arguments?.getString("imageId") ?: ""
                ImageDetailScreen(
                    imageId = imageId,
                    onBackClick = { navController.navigateUp() }
                )
            }

            composable(
                route = "clip?videoId={videoId}",
                arguments = listOf(navArgument("videoId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                })
            ) { backStackEntry ->
                val videoId = backStackEntry.arguments?.getString("videoId")
                ClipScreen(videoId = videoId)
            }

            composable("settings") {
                EnhancedSettingsScreen()
            }

            composable("search") {
                SearchScreen(
                    onNavigateBack = { navController.navigateUp() },
                    onVideoClick = { video ->
                        navController.navigate("video/${video.id}")
                    }
                )
            }
        }
    }
}
