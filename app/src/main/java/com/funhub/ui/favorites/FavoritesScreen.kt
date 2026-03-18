package com.funhub.ui.favorites

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.PlayCircle
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
fun FavoritesScreen(
    navController: NavController,
    videoViewModel: VideoListViewModel = hiltViewModel(),
    imageViewModel: ImageListViewModel = hiltViewModel()
) {
    val videoUiState by videoViewModel.uiState.collectAsState()
    val imageUiState by imageViewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    val favoriteVideos = videoUiState.videos.filter { it.isFavorite }
    val favoriteImages = imageUiState.images.filter { it.isFavorite }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的收藏") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("视频 ${favoriteVideos.size}") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("图片 ${favoriteImages.size}") }
                )
            }

            // Simple content switch based on tab
            Box(modifier = Modifier.fillMaxSize()) {
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

@Composable
fun FavoriteVideosGrid(
    videos: List<Video>,
    onVideoClick: (String) -> Unit,
    onUnfavorite: (String) -> Unit
) {
    if (videos.isEmpty()) {
        EmptyFavoritesView(
            icon = Icons.Outlined.PlayCircle,
            message = "暂无收藏的视频"
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(videos, key = { it.id }) { video ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 10f)
                        .clickable { onVideoClick(video.id) },
                    shape = RoundedCornerShape(12.dp)
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
                                imageVector = Icons.Filled.Favorite,
                                contentDescription = "取消收藏",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                        Text(
                            text = video.title,
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = 1,
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(8.dp)
                        )
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
        EmptyFavoritesView(
            icon = Icons.Outlined.Image,
            message = "暂无收藏的图片"
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(160.dp),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(images, key = { it.id }) { image ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clickable { onImageClick(image.id) },
                    shape = RoundedCornerShape(12.dp)
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
                                imageVector = Icons.Filled.Favorite,
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
fun EmptyFavoritesView(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    message: String
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}