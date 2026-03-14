package com.funhub.android.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.funhub.android.ui.components.QuickActionCard

/**
 * 首页仪表盘
 * 
 * 显示快速入口、最近播放、收藏内容等
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToVideos: () -> Unit,
    onNavigateToImages: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToShortVideo: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "FunHub",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "设置"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "首页") },
                    label = { Text("首页") },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.VideoLibrary, contentDescription = "视频") },
                    label = { Text("视频") },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onNavigateToVideos()
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Image, contentDescription = "图片") },
                    label = { Text("图片") },
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                        onNavigateToImages()
                    }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 短视频入口
            Card(
                onClick = onNavigateToShortVideo,
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "短视频",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "沉浸式观看体验",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.SmartDisplay,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }

            // 快速操作
            Text(
                text = "快速访问",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(quickActions) { action ->
                    QuickActionCard(
                        icon = action.icon,
                        title = action.title,
                        subtitle = action.subtitle,
                        onClick = action.onClick
                    )
                }
            }

            // 最近播放（占位）
            Text(
                text = "最近播放",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "暂无播放记录",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

data class QuickAction(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit
)

val quickActions = listOf(
    QuickAction(
        icon = Icons.Default.VideoLibrary,
        title = "视频库",
        subtitle = "浏览所有视频",
        onClick = { }
    ),
    QuickAction(
        icon = Icons.Default.Image,
        title = "图片库",
        subtitle = "浏览所有图片",
        onClick = { }
    ),
    QuickAction(
        icon = Icons.Default.Favorite,
        title = "收藏",
        subtitle = "查看收藏内容",
        onClick = { }
    ),
    QuickAction(
        icon = Icons.Default.History,
        title = "历史",
        subtitle = "观看历史记录",
        onClick = { }
    )
)
