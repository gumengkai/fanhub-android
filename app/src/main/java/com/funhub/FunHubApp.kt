package com.funhub

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.funhub.ui.clips.ClipScreen
import com.funhub.ui.components.BiliBottomNavigation
import com.funhub.ui.components.BiliNavItem
import com.funhub.ui.images.ImageListScreen
import com.funhub.ui.profile.ProfileScreen
import com.funhub.ui.search.SearchScreen
import com.funhub.ui.settings.EnhancedSettingsScreen
import com.funhub.ui.tags.TagsScreen
import com.funhub.ui.theme.BiliPink
import com.funhub.ui.videos.TikTokStyleVideoScreen
import com.funhub.ui.videos.VideoListScreen
import com.funhub.ui.videos.VideoPlayerScreen

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object ShortVideo : BottomNavItem("short_video", "短视频", Icons.Filled.PlayCircle, Icons.Outlined.PlayCircle)
    data object VideoLibrary : BottomNavItem("video_library", "视频库", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary)
    data object ImageLibrary : BottomNavItem("image_library", "图片库", Icons.Filled.Image, Icons.Outlined.Image)
    data object Profile : BottomNavItem("profile", "我的", Icons.Filled.Person, Icons.Outlined.Person)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunHubApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem.ShortVideo,
        BottomNavItem.VideoLibrary,
        BottomNavItem.ImageLibrary,
        BottomNavItem.Profile
    )

    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    val biliNavItems = listOf(
        BiliNavItem(
            label = "短视频",
            selectedIcon = Icons.Filled.PlayCircle,
            unselectedIcon = Icons.Outlined.PlayCircle
        ),
        BiliNavItem(
            label = "视频库",
            selectedIcon = Icons.Filled.VideoLibrary,
            unselectedIcon = Icons.Outlined.VideoLibrary
        ),
        BiliNavItem(
            label = "图片库",
            selectedIcon = Icons.Filled.Image,
            unselectedIcon = Icons.Outlined.Image
        ),
        BiliNavItem(
            label = "我的",
            selectedIcon = Icons.Filled.Person,
            unselectedIcon = Icons.Outlined.Person
        )
    )

    val currentRoute = currentDestination?.route
    val selectedIndex = when (currentRoute) {
        BottomNavItem.ShortVideo.route -> 0
        BottomNavItem.VideoLibrary.route -> 1
        BottomNavItem.ImageLibrary.route -> 2
        BottomNavItem.Profile.route -> 3
        else -> 0
    }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BiliBottomNavigation(
                    items = biliNavItems,
                    selectedIndex = selectedIndex,
                    onItemSelected = { index ->
                        val route = when (index) {
                            0 -> BottomNavItem.ShortVideo.route
                            1 -> BottomNavItem.VideoLibrary.route
                            2 -> BottomNavItem.ImageLibrary.route
                            3 -> BottomNavItem.Profile.route
                            else -> BottomNavItem.ShortVideo.route
                        }
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.ShortVideo.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // 短视频 - 抖音风格全屏播放
            composable(BottomNavItem.ShortVideo.route) {
                TikTokStyleVideoScreen()
            }

            // 视频库 - 视频列表
            composable(BottomNavItem.VideoLibrary.route) {
                VideoListScreen(
                    onVideoClick = { videoId ->
                        navController.navigate("video/$videoId")
                    },
                    onSearchClick = {
                        navController.navigate("search")
                    }
                )
            }

            // 图片库 - 图片列表
            composable(BottomNavItem.ImageLibrary.route) {
                ImageListScreen(
                    onImageClick = { imageId ->
                        navController.navigate("image/$imageId")
                    }
                )
            }

            // 个人中心 - 包含收藏和设置入口
            composable(BottomNavItem.Profile.route) {
                ProfileScreen(
                    navController = navController,
                    onSettingsClick = {
                        navController.navigate("settings")
                    },
                    onFavoritesClick = {
                        navController.navigate("favorites")
                    }
                )
            }

            // Video Player
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

            // Image Detail
            composable(
                route = "image/{imageId}",
                arguments = listOf(navArgument("imageId") { type = NavType.StringType })
            ) { backStackEntry ->
                val imageId = backStackEntry.arguments?.getString("imageId") ?: ""
                // TODO: Create ImageDetailScreen
            }

            // Clip Screen
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

            // Settings - 从个人中心跳转
            composable("settings") {
                EnhancedSettingsScreen(
                    onNavigateToTags = {
                        navController.navigate("tags")
                    }
                )
            }

            // Tags - 标签管理
            composable("tags") {
                TagsScreen()
            }

            // Favorites - 从个人中心跳转
            composable("favorites") {
                // TODO: Create FavoritesScreen
            }

            // Search
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
