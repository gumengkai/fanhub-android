package com.funhub.ui.tags

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsScreen(
    viewModel: TagsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var newTagName by remember { mutableStateOf("") }
    var newTagColor by remember { mutableStateOf("#1890ff") }
    var editTagName by remember { mutableStateOf("") }
    var editTagColor by remember { mutableStateOf("") }

    val predefinedColors = listOf(
        "#1890ff", "#52c41a", "#faad14", "#f5222d",
        "#722ed1", "#eb2f96", "#13c2c2", "#fa8c16"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("标签管理") },
                actions = {
                    IconButton(onClick = viewModel::showAddDialog) {
                        Icon(Icons.Default.Add, "添加标签")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                uiState.tags.isEmpty() -> {
                    Text(
                        "暂无标签",
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.tags, key = { it.id }) { tag ->
                            TagItem(
                                tag = tag,
                                onEdit = { viewModel.showEditDialog(tag) },
                                onDelete = { viewModel.deleteTag(tag.id) }
                            )
                        }
                    }
                }
            }
        }
    }

    // Add Tag Dialog
    if (uiState.showAddDialog) {
        AlertDialog(
            onDismissRequest = viewModel::hideAddDialog,
            title = { Text("添加标签") },
            text = {
                Column {
                    OutlinedTextField(
                        value = newTagName,
                        onValueChange = { newTagName = it },
                        label = { Text("标签名称") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("选择颜色", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        predefinedColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(android.graphics.Color.parseColor(color)))
                                    .clickable { newTagColor = color }
                                    .then(
                                        if (newTagColor == color) {
                                            Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                        } else Modifier
                                    )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newTagName.isNotBlank()) {
                            viewModel.createTag(newTagName, newTagColor)
                            newTagName = ""
                        }
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideAddDialog) {
                    Text("取消")
                }
            }
        )
    }

    // Edit Tag Dialog
    if (uiState.showEditDialog && uiState.selectedTag != null) {
        val tag = uiState.selectedTag!!
        LaunchedEffect(tag) {
            editTagName = tag.name
            editTagColor = tag.color
        }

        AlertDialog(
            onDismissRequest = viewModel::hideEditDialog,
            title = { Text("编辑标签") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editTagName,
                        onValueChange = { editTagName = it },
                        label = { Text("标签名称") },
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("选择颜色", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        predefinedColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(android.graphics.Color.parseColor(color)))
                                    .clickable { editTagColor = color }
                                    .then(
                                        if (editTagColor == color) {
                                            Modifier.border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                        } else Modifier
                                    )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (editTagName.isNotBlank()) {
                            viewModel.updateTag(tag.id, editTagName, editTagColor)
                        }
                    }
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::hideEditDialog) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
fun TagItem(
    tag: com.funhub.data.remote.dto.TagDto,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Color(android.graphics.Color.parseColor(tag.color)))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = tag.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }

            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Edit, "编辑")
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, "删除", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}