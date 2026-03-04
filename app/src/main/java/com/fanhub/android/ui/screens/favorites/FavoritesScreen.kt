package com.fanhub.android.ui.screens.favorites

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.fanhub.android.data.model.FavoriteItem
import com.fanhub.android.data.repository.Result
import com.fanhub.android.ui.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onNavigateBack: () -> Unit,
    onVideoClick: (Int) -> Unit,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedType by remember { mutableStateOf<FavoriteType?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的收藏", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    // 类型筛选
                    SegmentedButton(
                        selectedType = selectedType,
                        onTypeSelected = { selectedType = it }
                    )
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
            // 统计信息
            val stats = (uiState.stats as? com.fanhub.android.data.repository.Result.Success)?.data
            stats?.let { favoriteStats ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("全部", favoriteStats.total)
                        StatItem("视频", favoriteStats.totalVideos)
                        StatItem("图片", favoriteStats.totalImages)
                    }
                }
            }
            
            // 收藏列表
            when (val state = uiState.favorites) {
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
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.FavoriteBorder,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    "暂无收藏",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            contentPadding = PaddingValues(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.data.items) { item ->
                                FavoriteCard(
                                    item = item,
                                    onClick = {
                                        if (item.type == "video") {
                                            onVideoClick(item.id)
                                        }
                                    },
                                    onRemove = { viewModel.removeFromFavorites(item.id, item.type) }
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
                            Button(onClick = { viewModel.loadFavorites() }) {
                                Text("重试")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(label: String, value: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SegmentedButton(
    selectedType: FavoriteType?,
    onTypeSelected: (FavoriteType?) -> Unit
) {
    Row(
        modifier = Modifier.padding(end = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FilterChip(
            selected = selectedType == null,
            onClick = { onTypeSelected(null) },
            label = { Text("全部") },
            leadingIcon = if (selectedType == null) {
                { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(18.dp)) }
            } else null
        )
        FilterChip(
            selected = selectedType == FavoriteType.VIDEO,
            onClick = { onTypeSelected(FavoriteType.VIDEO) },
            label = { Text("视频") },
            leadingIcon = if (selectedType == FavoriteType.VIDEO) {
                { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(18.dp)) }
            } else null
        )
        FilterChip(
            selected = selectedType == FavoriteType.IMAGE,
            onClick = { onTypeSelected(FavoriteType.IMAGE) },
            label = { Text("图片") },
            leadingIcon = if (selectedType == FavoriteType.IMAGE) {
                { Icon(Icons.Default.Done, contentDescription = null, modifier = Modifier.size(18.dp)) }
            } else null
        )
    }
}

@Composable
private fun FavoriteCard(
    item: FavoriteItem,
    onClick: () -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box {
            Column {
                // 缩略图
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                ) {
                    AsyncImage(
                        model = item.thumbnailUrl,
                        contentDescription = item.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    
                    // 类型标签
                    Surface(
                        color = if (item.type == "video") Color(0xFFFB7299) else Color(0xFF5C9EFF),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = if (item.type == "video") Icons.Default.PlayArrow else Icons.Default.Image,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
                
                // 标题
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp),
                    maxLines = 2,
                    fontWeight = FontWeight.Medium
                )
            }
            
            // 移除按钮
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(4.dp)
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "移除",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

enum class FavoriteType {
    VIDEO,
    IMAGE
}
