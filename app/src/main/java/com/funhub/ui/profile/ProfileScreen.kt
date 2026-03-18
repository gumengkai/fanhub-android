package com.funhub.ui.profile

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
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
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: VideoListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val pagerState = rememberPagerState(pageCount = { 2 })
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("个人主页") },
                actions = {
                    IconButton(onClick = { }) {
                        Icon(Icons.Outlined.Menu, null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            // Profile Header
            ProfileHeader()

            // Stats
            ProfileStats()

            // Action Buttons
            ProfileActions()

            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Black,
                contentColor = Color.White
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("作品 ${uiState.videos.size}") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("喜欢") }
                )
            }

            // Content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> VideoGrid(
                        videos = uiState.videos,
                        onVideoClick = { videoId ->
                            navController.navigate("video/$videoId")
                        }
                    )
                    1 -> EmptyLikes()
                }
            }
        }
    }
}

@Composable
fun ProfileHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Surface(
            shape = CircleShape,
            color = Color.Gray,
            modifier = Modifier.size(100.dp)
        ) {
            AsyncImage(
                model = null,
                contentDescription = "头像",
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username
        Text(
            text = "用户名",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        // User ID
        Text(
            text = "抖音号: 123456789",
            color = Color.Gray,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Bio
        Text(
            text = "这个人很懒，什么都没有写~",
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ProfileStats() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem("0", "获赞")
        StatItem("0", "关注")
        StatItem("0", "粉丝")
    }
}

@Composable
fun StatItem(count: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            color = Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ProfileActions() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = { },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFE2C55)
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("关注")
        }

        OutlinedButton(
            onClick = { },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text("私信")
        }

        OutlinedButton(
            onClick = { },
            modifier = Modifier.width(48.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(4.dp)
        ) {
            Icon(Icons.Outlined.Add, null)
        }
    }
}

@Composable
fun VideoGrid(
    videos: List<Video>,
    onVideoClick: (String) -> Unit
) {
    if (videos.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("暂无作品", color = Color.Gray)
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize()
        ) {
            items(videos) { video ->
                VideoThumbnail(
                    video = video,
                    onClick = { onVideoClick(video.id) }
                )
            }
        }
    }
}

@Composable
fun VideoThumbnail(
    video: Video,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .aspectRatio(3f / 4f)
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = video.thumbnailUrl,
            contentDescription = video.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Play count overlay
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.PlayArrow,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = formatCount(video.viewCount),
                color = Color.White,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun EmptyLikes() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.FavoriteBorder,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("暂无喜欢的作品", color = Color.Gray)
        }
    }
}

fun formatCount(count: Int): String {
    return when {
        count >= 10000 -> "${count / 10000}w"
        count >= 1000 -> "${count / 1000}k"
        else -> count.toString()
    }
}
