package com.funhub.android.ui.screens.video

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.funhub.android.data.api.models.Video
import com.funhub.android.ui.components.VideoCard

/**
 * 视频库页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoLibraryScreen(
    onNavigateBack: () -> Unit,
    onVideoClick: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }

    // 模拟视频数据（实际应从 API 获取）
    val videos = remember { emptyList<Video>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("视频库") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (showSearch) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("搜索视频") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            singleLine = true
                        )
                    } else {
                        IconButton(onClick = { showSearch = true }) {
                            Icon(Icons.Default.Search, contentDescription = "搜索")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        if (videos.isEmpty()) {
            // 空状态
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = "暂无视频",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 280.dp),
                contentPadding = paddingValues,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(videos, key = { it.id }) { video ->
                    VideoCard(
                        video = video,
                        baseUrl = "", // TODO: 从设置获取
                        onClick = { onVideoClick(video.id) },
                        onFavoriteToggle = { /* TODO: 实现收藏 */ }
                    )
                }
            }
        }
    }
}
