package com.funhub.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.funhub.domain.model.Image
import com.funhub.domain.model.Video
import com.funhub.ui.components.EmptyView
import com.funhub.ui.components.ErrorView
import com.funhub.ui.components.LoadingIndicator

enum class ContentFilter { ALL, VIDEOS, IMAGES }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onVideoClick: (String) -> Unit,
    onImageClick: (String) -> Unit,
    onSearchClick: () -> Unit,
    onTikTokClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFilter by remember { mutableStateOf(ContentFilter.ALL) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "FunHub",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(Icons.Default.Search, null, Modifier.size(28.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onTikTokClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Outlined.PlayCircle, "短视频")
            }
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            FilterChipsRow(selectedFilter, { selectedFilter = it })
            
            Box(Modifier.fillMaxSize()) {
                when {
                    uiState.isLoading -> LoadingIndicator()
                    uiState.error != null -> ErrorView(message = uiState.error!!, onRetry = viewModel::loadContent)
                    selectedFilter == ContentFilter.ALL -> MixedContentGrid(
                        uiState.videos, uiState.images,
                        onVideoClick, onImageClick,
                        viewModel::toggleVideoFavorite, viewModel::toggleImageFavorite
                    )
                    selectedFilter == ContentFilter.VIDEOS -> VideoContentGrid(
                        uiState.videos, onVideoClick, viewModel::toggleVideoFavorite
                    )
                    else -> ImageContentGrid(
                        uiState.images, onImageClick, viewModel::toggleImageFavorite
                    )
                }
            }
        }
    }
}

@Composable
fun FilterChipsRow(selected: ContentFilter, onSelect: (ContentFilter) -> Unit) {
    Row(Modifier.fillMaxWidth().padding(16.dp, 8.dp), Arrangement.spacedBy(8.dp)) {
        FilterChip(selected == ContentFilter.ALL, { onSelect(ContentFilter.ALL) }, { Text("全部") })
        FilterChip(selected == ContentFilter.VIDEOS, { onSelect(ContentFilter.VIDEOS) }, { Text("视频") })
        FilterChip(selected == ContentFilter.IMAGES, { onSelect(ContentFilter.IMAGES) }, { Text("图片") })
    }
}

@Composable
fun MixedContentGrid(
    videos: List<Video>, images: List<Image>,
    onVideoClick: (String) -> Unit, onImageClick: (String) -> Unit,
    onVideoFavorite: (String, Boolean) -> Unit, onImageFavorite: (String, Boolean) -> Unit
) {
    val mixed = (videos.map { Pair(it, true) } + images.map { Pair(it, false) }).shuffled()
    
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(mixed, key = { (item, isVideo) -> if (isVideo) (item as Video).id else (item as Image).id }) { (item, isVideo) ->
            if (isVideo) {
                ModernVideoCard(item as Video, onVideoClick, onVideoFavorite)
            } else {
                ModernImageCard(item as Image, onImageClick, onImageFavorite)
            }
        }
    }
}

@Composable
fun VideoContentGrid(videos: List<Video>, onClick: (String) -> Unit, onFavorite: (String, Boolean) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(videos, key = { it.id }) { video ->
            ModernVideoCard(video, onClick, onFavorite)
        }
    }
}

@Composable
fun ImageContentGrid(images: List<Image>, onClick: (String) -> Unit, onFavorite: (String, Boolean) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(images, key = { it.id }) { image ->
            ModernImageCard(image, onClick, onFavorite)
        }
    }
}

@Composable
fun ModernVideoCard(
    video: Video,
    onClick: (String) -> Unit,
    onFavorite: (String, Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f)

    Card(
        Modifier.scale(scale).clickable(interactionSource, null) { onClick(video.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp)
    ) {
        Column {
            Box(Modifier.fillMaxWidth().aspectRatio(16f / 10f)) {
                AsyncImage(
                    video.thumbnailUrl,
                    null,
                    Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(Modifier.fillMaxSize().background(Brush.radialGradient(
                    listOf(Color.Black.copy(0.4f), Color.Transparent)
                )), Alignment.Center) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary.copy(0.9f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(Icons.Default.PlayArrow, null, Modifier.fillMaxSize().padding(8.dp), Color.White)
                    }
                }
                IconButton({ onFavorite(video.id, !video.isFavorite) }, Modifier.align(Alignment.TopEnd)) {
                    Icon(
                        if (video.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null,
                        tint = if (video.isFavorite) MaterialTheme.colorScheme.primary else Color.White.copy(0.8f)
                    )
                }
            }
            Text(
                video.title,
                Modifier.padding(12.dp),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                maxLines = 2
            )
        }
    }
}

@Composable
fun ModernImageCard(
    image: Image,
    onClick: (String) -> Unit,
    onFavorite: (String, Boolean) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.96f else 1f)

    Card(
        Modifier.scale(scale).clickable(interactionSource, null) { onClick(image.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp, pressedElevation = 8.dp)
    ) {
        Box(Modifier.fillMaxWidth().aspectRatio(1f)) {
            AsyncImage(
                image.thumbnailUrl ?: image.url,
                null,
                Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(Modifier.fillMaxSize().background(Brush.verticalGradient(
                listOf(Color.Transparent, Color.Transparent, Color.Black.copy(0.6f))
            )))
            IconButton({ onFavorite(image.id, !image.isFavorite) }, Modifier.align(Alignment.TopEnd)) {
                Icon(
                    if (image.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    null,
                    tint = if (image.isFavorite) MaterialTheme.colorScheme.primary else Color.White.copy(0.8f)
                )
            }
            Text(
                image.title,
                Modifier.align(Alignment.BottomStart).padding(12.dp),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium, color = Color.White),
                maxLines = 1
            )
        }
    }
}
