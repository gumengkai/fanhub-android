package com.funhub.ui.feed

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.funhub.domain.model.Video
import com.funhub.ui.videos.VideoListViewModel

enum class FeedType { HOME, DISCOVER }

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeedScreen(
    navController: NavController,
    feedType: FeedType,
    viewModel: VideoListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { maxOf(uiState.videos.size, 1) })

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.videos.isEmpty()) {
            EmptyFeed()
        } else {
            VerticalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val video = uiState.videos.getOrNull(page)
                if (video != null) {
                    VideoItem(
                        video = video,
                        isPlaying = page == pagerState.currentPage,
                        onDoubleTap = { viewModel.toggleFavorite(video.id, !video.isFavorite) },
                        onLikeClick = { viewModel.toggleFavorite(video.id, !video.isFavorite) },
                        onCommentClick = { },
                        onShareClick = { }
                    )
                }
            }
        }

        FeedTopBar(
            feedType = feedType,
            onTabSelected = { },
            onSearchClick = { navController.navigate("search") }
        )
    }
}

@Composable
fun EmptyFeed() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun FeedTopBar(
    feedType: FeedType,
    onTabSelected: (FeedType) -> Unit,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 48.dp, start = 16.dp, end = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onSearchClick) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = "搜索",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FeedTab(
                text = "推荐",
                isSelected = feedType == FeedType.HOME,
                onClick = { onTabSelected(FeedType.HOME) }
            )
            FeedTab(
                text = "发现",
                isSelected = feedType == FeedType.DISCOVER,
                onClick = { onTabSelected(FeedType.DISCOVER) }
            )
        }

        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.Outlined.LiveTv,
                contentDescription = "直播",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun FeedTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.1f else 1f,
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.6f,
        label = "alpha"
    )

    Text(
        text = text,
        color = Color.White,
        fontSize = if (isSelected) 18.sp else 16.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .scale(scale)
            .alpha(alpha)
            .clickable(onClick = onClick)
    )
}

@Composable
fun VideoItem(
    video: Video,
    isPlaying: Boolean,
    onDoubleTap: () -> Unit,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Background
        AsyncImage(
            model = video.thumbnailUrl,
            contentDescription = video.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
        )

        // Right side actions
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Avatar
            Surface(
                shape = CircleShape,
                color = Color.Gray,
                modifier = Modifier.size(48.dp)
            ) {
                AsyncImage(
                    model = video.creatorAvatar,
                    contentDescription = video.creatorName,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Like
            VideoActionButton(
                icon = {
                    Icon(
                        imageVector = if (video.isFavorite)
                            Icons.Filled.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = "点赞",
                        tint = if (video.isFavorite) Color(0xFFFE2C55) else Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                },
                label = formatCount(video.likeCount),
                onClick = onLikeClick
            )

            // Comment
            VideoActionButton(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Comment,
                        contentDescription = "评论",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                },
                label = "评论",
                onClick = onCommentClick
            )

            // Share
            VideoActionButton(
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Share,
                        contentDescription = "分享",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                },
                label = "分享",
                onClick = onShareClick
            )
        }

        // Bottom info
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "@${video.creatorName}",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = video.title,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.MusicNote,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "原创音乐",
                    color = Color.White,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun VideoActionButton(
    icon: @Composable () -> Unit,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        icon()
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

fun formatCount(count: Int): String {
    return when {
        count >= 10000 -> "${count / 10000}w"
        count >= 1000 -> "${count / 1000}k"
        else -> count.toString()
    }
}
