package com.funhub.android.ui.screens.image

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
import com.funhub.android.data.api.models.Image
import com.funhub.android.ui.components.ImageCard

/**
 * 图片库页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageLibraryScreen(
    onNavigateBack: () -> Unit,
    onImageClick: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var showSearch by remember { mutableStateOf(false) }

    // 模拟图片数据（实际应从 API 获取）
    val images = remember { emptyList<Image>() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("图片库") },
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
                            placeholder = { Text("搜索图片") },
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
        if (images.isEmpty()) {
            // 空状态
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    text = "暂无图片",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 120.dp),
                contentPadding = paddingValues,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(images, key = { it.id }) { image ->
                    ImageCard(
                        image = image,
                        baseUrl = "", // TODO: 从设置获取
                        onClick = { onImageClick(image.id) },
                        onFavoriteToggle = { /* TODO: 实现收藏 */ }
                    )
                }
            }
        }
    }
}
