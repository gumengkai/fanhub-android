package com.funhub.ui.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VideoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.funhub.domain.model.Image
import com.funhub.domain.model.Video
import com.funhub.ui.images.ImageListViewModel
import com.funhub.ui.videos.VideoListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    onSettingsClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    videoViewModel: VideoListViewModel = hiltViewModel(),
    imageViewModel: ImageListViewModel = hiltViewModel()
) {
    val videoUiState by videoViewModel.uiState.collectAsState()
    val imageUiState by imageViewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    val videos = videoUiState.videos
    val images = imageUiState.images
    val favoriteVideos = videos.filter { it.isFavorite }
    val favoriteImages = images.filter { it.isFavorite }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("个人中心") },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(Icons.Outlined.Settings, "设置")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Stats Row
            StatsRow(
                videoCount = videos.size,
                imageCount = images.size,
                favoriteVideoCount = favoriteVideos.size,
                favoriteImageCount = favoriteImages.size
            )

            // Favorites Section with Tabs
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column {
                    // Header with Tabs
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "我的收藏",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        // Tabs
                        Row {
                            FilterChip(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                label = { Text("视频 ${favoriteVideos.size}") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.VideoLibrary,
                                        null,
                                        Modifier.size(18.dp)
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            FilterChip(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                label = { Text("图片 ${favoriteImages.size}") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Outlined.Image,
                                        null,
                                        Modifier.size(18.dp)
                                    )
                                }
                            )
                        }
                    }

                    // Content
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp)
                    ) {
                        when (selectedTab) {
                            0 -> FavoriteVideosGrid(
                                videos = favoriteVideos,
                                onVideoClick = { navController.navigate("video/$it") },
                                onUnfavorite = { videoViewModel.toggleFavorite(it, false) }
                            )
                            1 -> FavoriteImagesGrid(
                                images = favoriteImages,
                                onImageClick = { navController.navigate("image/$it") },
                                onUnfavorite = { imageViewModel.toggleFavorite(it, false) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatsRow(
    videoCount: Int,
    imageCount: Int,
    favoriteVideoCount: Int,
    favoriteImageCount: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                icon = Icons.Outlined.VideoLibrary,
                count = videoCount.toString(),
                label = "视频"
            )
            StatItem(
                icon = Icons.Outlined.Image,
                count = imageCount.toString(),
                label = "图片"
            )
            StatItem(
                icon = Icons.Default.Favorite,
                count = (favoriteVideoCount + favoriteImageCount).toString(),
                label = "收藏"
            )
        }
    }
}

@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    count: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = count,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun FavoriteVideosGrid(
    videos: List<Video>,
    onVideoClick: (String) -> Unit,
    onUnfavorite: (String) -> Unit
) {
    if (videos.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("暂无收藏的视频", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(videos, key = { it.id }) { video ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                        .padding(4.dp)
                        .clickable { onVideoClick(video.id) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = video.thumbnailUrl,
                            contentDescription = video.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { onUnfavorite(video.id) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "取消收藏",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteImagesGrid(
    images: List<Image>,
    onImageClick: (String) -> Unit,
    onUnfavorite: (String) -> Unit
) {
    if (images.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("暂无收藏的图片", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(100.dp),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(images, key = { it.id }) { image ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(4.dp)
                        .clickable { onImageClick(image.id) },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = image.thumbnailUrl ?: image.url,
                            contentDescription = image.title,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        IconButton(
                            onClick = { onUnfavorite(image.id) },
                            modifier = Modifier.align(Alignment.TopEnd)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "取消收藏",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}