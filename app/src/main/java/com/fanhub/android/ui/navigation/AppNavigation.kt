package com.fanhub.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fanhub.android.ui.screens.favorites.FavoritesScreen
import com.fanhub.android.ui.screens.home.HomeScreen
import com.fanhub.android.ui.screens.images.ImageLibraryScreen
import com.fanhub.android.ui.screens.settings.SettingsScreen
import com.fanhub.android.ui.screens.shortvideo.ShortVideoScreen
import com.fanhub.android.ui.screens.videos.VideoLibraryScreen
import com.fanhub.android.ui.screens.videos.VideoPlayerScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object VideoLibrary : Screen("videos")
    object ImageLibrary : Screen("images")
    object ShortVideo : Screen("short_video")
    object Favorites : Screen("favorites")
    object Settings : Screen("settings")
    object VideoPlayer : Screen("video_player/{videoId}") {
        fun createRoute(videoId: Int) = "video_player/$videoId"
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToVideos = { navController.navigate(Screen.VideoLibrary.route) },
                onNavigateToImages = { navController.navigate(Screen.ImageLibrary.route) },
                onNavigateToShortVideo = { navController.navigate(Screen.ShortVideo.route) },
                onNavigateToFavorites = { navController.navigate(Screen.Favorites.route) }
            )
        }
        
        composable(Screen.VideoLibrary.route) {
            VideoLibraryScreen(
                onNavigateBack = { navController.popBackStack() },
                onVideoClick = { videoId ->
                    navController.navigate(Screen.VideoPlayer.createRoute(videoId))
                }
            )
        }
        
        composable(Screen.ImageLibrary.route) {
            ImageLibraryScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.ShortVideo.route) {
            ShortVideoScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onNavigateBack = { navController.popBackStack() },
                onVideoClick = { videoId ->
                    navController.navigate(Screen.VideoPlayer.createRoute(videoId))
                }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(Screen.VideoPlayer.route) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId")?.toIntOrNull() ?: return@composable
            VideoPlayerScreen(
                videoId = videoId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
