package com.funhub.android.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.funhub.android.ui.screens.home.HomeScreen
import com.funhub.android.ui.screens.image.ImageLibraryScreen
import com.funhub.android.ui.screens.settings.SettingsScreen
import com.funhub.android.ui.screens.video.VideoLibraryScreen

/**
 * 应用导航图
 * 
 * 定义所有页面路由和导航逻辑
 */
@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        // 首页
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToVideos = { navController.navigate(Screen.VideoLibrary.route) },
                onNavigateToImages = { navController.navigate(Screen.ImageLibrary.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToShortVideo = { navController.navigate(Screen.ShortVideo.route) }
            )
        }

        // 视频库
        composable(Screen.VideoLibrary.route) {
            VideoLibraryScreen(
                onNavigateBack = { navController.popBackStack() },
                onVideoClick = { videoId ->
                    // TODO: 导航到视频播放器
                }
            )
        }

        // 图片库
        composable(Screen.ImageLibrary.route) {
            ImageLibraryScreen(
                onNavigateBack = { navController.popBackStack() },
                onImageClick = { imageId ->
                    // TODO: 导航到图片查看器
                }
            )
        }

        // 设置
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // 短视频
        composable(Screen.ShortVideo.route) {
            // TODO: 实现短视频屏幕
            HomeScreen(
                onNavigateToVideos = { },
                onNavigateToImages = { },
                onNavigateToSettings = { },
                onNavigateToShortVideo = { }
            )
        }
    }
}

/**
 * 路由定义
 */
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object VideoLibrary : Screen("videos")
    object ImageLibrary : Screen("images")
    object Settings : Screen("settings")
    object ShortVideo : Screen("short-video")
    object VideoPlayer : Screen("video-player/{videoId}") {
        fun createRoute(videoId: Int) = "video-player/$videoId"
    }
    object ImageViewer : Screen("image-viewer/{imageId}") {
        fun createRoute(imageId: Int) = "image-viewer/$imageId"
    }
}
