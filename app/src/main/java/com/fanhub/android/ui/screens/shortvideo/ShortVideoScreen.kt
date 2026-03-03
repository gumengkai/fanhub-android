package com.fanhub.android.ui.screens.shortvideo

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.fanhub.android.data.model.Video
import com.fanhub.android.data.repository.Result
import com.fanhub.android.ui.viewmodel.ShortVideoViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ShortVideoScreen(
    onNavigateBack: () -> Unit,
    viewModel: ShortVideoViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    
    var showFilterDialog by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("短视频", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "筛选")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = uiState.videos) {
                is Result.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                is Result.Success -> {
                    if (state.data.items.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("暂无视频")
                        }
                    } else {
                        ShortVideoPager(
                            videos = state.data.items,
                            onToggleFavorite = { viewModel.toggleFavorite(it) }
                        )
                    }
                }
                is Result.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("加载失败：${state.message}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.loadVideos() }) {
                                Text("重试")
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (showFilterDialog) {
        FilterDialog(
            onDismiss = { showFilterDialog = false },
            onApply = { /* TODO */ }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShortVideoPager(
    videos: List<Video>,
    onToggleFavorite: (Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { videos.size })
    val context = LocalContext.current
    
    // 自动播放当前视频
    LaunchedEffect(pagerState.currentPage) {
        // 暂停其他视频，播放当前视频
    }
    
    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragEnd = {
                        // 可以在这里添加滑动结束后的逻辑
                    }
                )
            }
    ) { page ->
        val video = videos[page]
        ShortVideoItem(
            video = video,
            isPlaying = pagerState.currentPage == page,
            onToggleFavorite = { onToggleFavorite(video.id) }
        )
    }
}

@Composable
private fun ShortVideoItem(
    video: Video,
    isPlaying: Boolean,
    onToggleFavorite: () -> Unit
) {
    val context = LocalContext.current
    var player by remember { mutableStateOf<ExoPlayer?>(null) }
    
    DisposableEffect(Unit) {
        player = ExoPlayer.Builder(context).build().apply {
            playWhenReady = isPlaying
            repeatMode = Player.REPEAT_MODE_ONE
        }
        
        onDispose {
            player?.release()
        }
    }
    
    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            player?.prepare()
            player?.play()
        } else {
            player?.pause()
        }
    }
    
    LaunchedEffect(video.id) {
        player?.setMediaItem(MediaItem.fromUri("http://192.168.1.100:8080/api/videos/${video.id}/stream"))
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 视频播放器
        AndroidView(
            factory = { PlayerView(it).apply {
                this.player = player
                useController = false // 隐藏默认控件
            }},
            modifier = Modifier.fillMaxSize()
        )
        
        // 右侧操作按钮
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 收藏按钮
            FloatingActionButton(
                onClick = onToggleFavorite,
                containerColor = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = if (video.isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "收藏",
                    tint = if (video.isFavorite) Color.Red else Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            // 标签按钮
            FloatingActionButton(
                onClick = { /* TODO: 显示标签 */ },
                containerColor = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Label,
                    contentDescription = "标签",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        
        // 底部信息
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
                .padding(bottom = 80.dp)
        ) {
            Text(
                text = video.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            if (video.description != null && video.description.isNotEmpty()) {
                Text(
                    text = video.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun FilterDialog(
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("筛选") },
        text = { Text("筛选功能开发中...") },
        confirmButton = {
            TextButton(onClick = {
                onApply()
                onDismiss()
            }) {
                Text("应用")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}
