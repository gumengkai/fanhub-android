package com.funhub.ui.main

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Message
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.funhub.ui.feed.FeedScreen
import com.funhub.ui.feed.FeedType
import com.funhub.ui.profile.ProfileScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navController: NavController
) {
    val pagerState = rememberPagerState(pageCount = { 5 })
    val scope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            TikTokBottomBar(
                currentPage = pagerState.currentPage,
                onPageSelected = { page ->
                    scope.launch {
                        pagerState.animateScrollToPage(page)
                    }
                },
                onAddClick = {
                    // TODO: Open upload dialog
                }
            )
        }
    ) { paddingValues ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) { page ->
            when (page) {
                0 -> FeedScreen(
                    navController = navController,
                    feedType = FeedType.HOME
                )
                1 -> FeedScreen(
                    navController = navController,
                    feedType = FeedType.DISCOVER
                )
                2 -> Box(Modifier.fillMaxSize()) // Add button placeholder
                3 -> MessagesScreen()
                4 -> ProfileScreen(navController = navController)
            }
        }
    }
}

@Composable
fun TikTokBottomBar(
    currentPage: Int,
    onPageSelected: (Int) -> Unit,
    onAddClick: () -> Unit
) {
    val items = listOf(
        BottomNavItem("首页", Icons.Outlined.Home),
        BottomNavItem("发现", Icons.Outlined.Search),
        BottomNavItem("", null), // Add button placeholder
        BottomNavItem("消息", Icons.Outlined.Message),
        BottomNavItem("我", Icons.Outlined.Person)
    )

    Surface(
        color = Color.Black,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                if (index == 2) {
                    // Add button
                    AddButton(onClick = onAddClick)
                } else {
                    BottomNavItem(
                        item = item,
                        isSelected = currentPage == index,
                        onClick = { onPageSelected(index) }
                    )
                }
            }
        }
    }
}

@Composable
fun AddButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = androidx.compose.ui.graphics.Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF25F4EE),
                        Color(0xFFFE2C55)
                    )
                )
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "添加",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
fun BottomNavItem(
    item: BottomNavItem,
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

    Column(
        modifier = Modifier
            .width(64.dp)
            .scale(scale)
            .alpha(alpha)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item.icon?.let {
            Icon(
                imageVector = it,
                contentDescription = item.label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        if (item.label.isNotEmpty()) {
            Text(
                text = item.label,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val icon: ImageVector?
)



@Composable
fun MessagesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("消息页面", color = Color.White)
    }
}
