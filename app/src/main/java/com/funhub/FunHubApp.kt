package com.funhub

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.funhub.ui.clips.ClipScreen
import com.funhub.ui.images.ImageDetailScreen
import com.funhub.ui.images.ImageListScreen
import com.funhub.ui.settings.SettingsScreen
import com.funhub.ui.videos.VideoListScreen
import com.funhub.ui.videos.VideoPlayerScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FunHubApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val showBottomBar = currentRoute in listOf(
        Screen.Videos.route,
        Screen.Images.route,
        Screen.Settings.route
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        when (currentRoute) {
                            Screen.Videos.route -> "视频库"
                            Screen.Images.route -> "图片库"
                            Screen.Settings.route -> "设置"
                            else -> "FunHub"
                        }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.startDestinationId) {
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
        NavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.VideoLibrary, contentDescription = null) },
            label = { Text("视频") },
            selected = currentRoute == Screen.Videos.route,
            onClick = { onNavigate(Screen.Videos.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Image, contentDescription = null) },
            label = { Text("图片") },
            selected = currentRoute == Screen.Images.route,
            onClick = { onNavigate(Screen.Images.route) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Settings, contentDescription = null) },
            label = { Text("设置") },
            selected = currentRoute == Screen.Settings.route,
            onClick = { onNavigate(Screen.Settings.route) }
        )
    }
}

@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Videos.route,
        modifier = modifier
    ) {
        composable(Screen.Videos.route) {
            VideoListScreen(
                onVideoClick = { videoId ->
                    navController.navigate(Screen.VideoPlayer.createRoute(videoId))
                }
            )
        }
        
        composable(
            route = Screen.VideoPlayer.route,
            arguments = listOf(navArgument("videoId") { type = NavType.StringType })
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId") ?: ""
            VideoPlayerScreen(
                videoId = videoId,
                onBackClick = { navController.navigateUp() },
                onClipClick = { navController.navigate(Screen.Clip.createRoute(it)) }
            )
        }
        
        composable(Screen.Images.route) {
            ImageListScreen(
                onImageClick = { imageId ->
                    navController.navigate(Screen.ImageDetail.createRoute(imageId))
                }
            )
        }
        
        composable(
            route = Screen.ImageDetail.route,
            arguments = listOf(navArgument("imageId") { type = NavType.StringType })
        ) { backStackEntry ->
            val imageId = backStackEntry.arguments?.getString("imageId") ?: ""
            ImageDetailScreen(
                imageId = imageId,
                onBackClick = { navController.navigateUp() }
            )
        }
        
        composable(
            route = Screen.Clip.route,
            arguments = listOf(navArgument("videoId") { 
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val videoId = backStackEntry.arguments?.getString("videoId")
            ClipScreen(videoId = videoId)
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}