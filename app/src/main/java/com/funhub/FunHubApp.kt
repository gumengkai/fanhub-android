package com.funhub

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
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
import com.funhub.ui.favorites.FavoritesScreen
import com.funhub.ui.images.ImageListScreen
import com.funhub.ui.search.SearchScreen
import com.funhub.ui.settings.EnhancedSettingsScreen
import com.funhub.ui.videos.VideoListScreen
import com.funhub.ui.videos.VideoPlayerScreen

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val unselectedIcon: androidx.compose.ui.graphics.vector.ImageVector
) {
    data object Home : BottomNavItem("home", "首页", Icons.Filled.Home, Icons.Outlined.Home)
    data object Videos : BottomNavItem("videos", "视频库", Icons.Filled.VideoLibrary, Icons.Outlined.VideoLibrary)
    data object Favorites : BottomNavItem("favorites", "收藏", Icons.Filled.Person, Icons.Outlined.Person)
    data object Settings : BottomNavItem("settings", "设置", Icons.Filled.Settings, Icons.Outlined.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunHubApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomNavItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Videos,
        BottomNavItem.Favorites,
        BottomNavItem.Settings
    )

    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            },
                            label = { Text(item.title) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
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
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Home - 混合展示视频和图片
            composable(BottomNavItem.Home.route) {
                VideoListScreen(
                    onVideoClick = { videoId ->
                        navController.navigate("video/$videoId")
                    },
                    onSearchClick = {
                        navController.navigate("search")
                    }
                )
            }

            // Video Library - 视频库
            composable(BottomNavItem.Videos.route) {
                ImageListScreen(
                    onImageClick = { imageId ->
                        navController.navigate("image/$imageId")
                    }
                )
            }

            // Favorites - 收藏（个人中心）
            composable(BottomNavItem.Favorites.route) {
                FavoritesScreen(
                    navController = navController
                )
            }

            // Settings - 系统设置
            composable(BottomNavItem.Settings.route) {
                EnhancedSettingsScreen()
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
