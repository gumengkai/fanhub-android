package com.fanhub.android.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BottomNavigationBar(
    onNavigateToVideos: () -> Unit,
    onNavigateToImages: () -> Unit,
    onNavigateToShortVideo: () -> Unit,
    onNavigateToFavorites: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.VideoLibrary, contentDescription = "视频库") },
            label = { Text("视频") },
            selected = false,
            onClick = onNavigateToVideos
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Image, contentDescription = "图片库") },
            label = { Text("图片") },
            selected = false,
            onClick = onNavigateToImages
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.PlayCircle, contentDescription = "短视频") },
            label = { Text("短视频") },
            selected = false,
            onClick = onNavigateToShortVideo
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Favorite, contentDescription = "收藏") },
            label = { Text("收藏") },
            selected = false,
            onClick = onNavigateToFavorites
        )
    }
}
