package com.fanhub.android.ui.screens.images

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fanhub.android.data.model.Image
import com.fanhub.android.data.repository.Result
import com.fanhub.android.ui.viewmodel.ImageLibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageLibraryScreen(
    onNavigateBack: () -> Unit,
    viewModel: ImageLibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSlideshow by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("图片库", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavoriteFilter() }) {
                        Icon(
                            if (viewModel.isFavoriteFilter) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "只看收藏"
                        )
                    }
                    IconButton(onClick = { showSlideshow = true }) {
                        Icon(Icons.Default.PlayCircle, contentDescription = "幻灯片")
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 搜索栏
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("搜索图片...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "清除")
                        }
                    }
                },
                shape = RoundedCornerShape(24.dp),
                singleLine = true
            )
            
            // 图片网格
            when (val state = uiState.images) {
                is Result.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Result.Success -> {
                    if (state.data.items.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("暂无图片")
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 120.dp),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.data.items) { image ->
                                ImageCard(
                                    image = image,
                                    onClick = { /* TODO: 查看大图 */ },
                                    onToggleFavorite = { viewModel.toggleFavorite(image.id) }
                                )
                            }
                        }
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
                            Button(onClick = { viewModel.loadImages() }) {
                                Text("重试")
                            }
                        }
                    }
                }
            }
        }
    }
    
    if (showSlideshow) {
        SlideshowDialog(
            images = (uiState.images as? Result.Success)?.data?.items ?: emptyList(),
            onDismiss = { showSlideshow = false }
        )
    }
}

@Composable
private fun ImageCard(
    image: Image,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box {
            AsyncImage(
                model = image.thumbnailPath ?: image.filePath,
                contentDescription = image.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                Icon(
                    imageVector = if (image.isFavorite) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "收藏",
                    tint = if (image.isFavorite) Color.Red else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun SlideshowDialog(
    images: List<Image>,
    onDismiss: () -> Unit
) {
    var currentIndex by remember { mutableStateOf(0) }
    var interval by remember { mutableStateOf(3) }
    var isPlaying by remember { mutableStateOf(false) }
    
    LaunchedEffect(isPlaying, currentIndex) {
        if (isPlaying && images.isNotEmpty()) {
            kotlinx.coroutines.delay(interval * 1000L)
            currentIndex = (currentIndex + 1) % images.size
        }
    }
    
    AlertDialog(
        onDismissRequest = {
            isPlaying = false
            onDismiss()
        },
        title = { Text("幻灯片播放 (${currentIndex + 1}/${images.size})") },
        text = {
            if (images.isNotEmpty()) {
                Column {
                    AsyncImage(
                        model = images[currentIndex].filePath,
                        contentDescription = images[currentIndex].title,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 9f)
                            .clip(RoundedCornerShape(8.dp))
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(
                            onClick = { currentIndex = (currentIndex - 1 + images.size) % images.size }
                        ) {
                            Icon(Icons.Default.ChevronLeft, contentDescription = "上一张")
                        }
                        
                        IconButton(onClick = { isPlaying = !isPlaying }) {
                            Icon(
                                if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "暂停" else "播放"
                            )
                        }
                        
                        IconButton(
                            onClick = { currentIndex = (currentIndex + 1) % images.size }
                        ) {
                            Icon(Icons.Default.ChevronRight, contentDescription = "下一张")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("播放间隔：${interval}秒")
                    Slider(
                        value = interval.toFloat(),
                        onValueChange = { interval = it.toInt() },
                        valueRange = 1f..10f,
                        steps = 9
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("关闭")
            }
        }
    )
}
