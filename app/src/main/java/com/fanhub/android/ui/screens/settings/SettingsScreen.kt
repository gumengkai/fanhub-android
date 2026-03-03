package com.fanhub.android.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fanhub.android.data.local.dataStore
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()
    
    var showApiUrlDialog by remember { mutableStateOf(false) }
    var tempApiUrl by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // 网络设置
            SettingsSection(title = "网络设置") {
                SettingsItem(
                    icon = Icons.Default.Dns,
                    title = "后端 API 地址",
                    subtitle = uiState.apiUrl.ifEmpty { "使用默认地址" },
                    onClick = {
                        tempApiUrl = uiState.apiUrl
                        showApiUrlDialog = true
                    }
                )
                
                if (uiState.apiUrl.isNotEmpty()) {
                    SettingsItem(
                        icon = Icons.Default.Refresh,
                        title = "重置为默认地址",
                        subtitle = BuildConfig.DEFAULT_API_URL,
                        onClick = {
                            scope.launch {
                                viewModel.resetApiUrl()
                            }
                        }
                    )
                }
            }
            
            // 关于
            SettingsSection(title = "关于") {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "应用版本",
                    subtitle = "1.0.0",
                    onClick = { }
                )
                
                SettingsItem(
                    icon = Icons.Default.Description,
                    title = "开源协议",
                    subtitle = "MIT License",
                    onClick = { }
                )
            }
            
            // 缓存
            SettingsSection(title = "存储") {
                SettingsItem(
                    icon = Icons.Default.DeleteOutline,
                    title = "清除缓存",
                    subtitle = "清除图片和视频缩略图缓存",
                    onClick = {
                        // TODO: 实现清除缓存
                    }
                )
            }
        }
    }
    
    // API 地址配置对话框
    if (showApiUrlDialog) {
        AlertDialog(
            onDismissRequest = { showApiUrlDialog = false },
            icon = { Icon(Icons.Default.Dns, contentDescription = null) },
            title = { Text("配置后端地址") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("输入 FanHub 后端的 API 地址")
                    OutlinedTextField(
                        value = tempApiUrl,
                        onValueChange = { tempApiUrl = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("例如：http://192.168.1.100:8080/api") },
                        singleLine = true,
                        isError = tempApiUrl.isNotEmpty() && !tempApiUrl.startsWith("http")
                    )
                    if (tempApiUrl.isNotEmpty() && !tempApiUrl.startsWith("http")) {
                        Text(
                            "地址必须以 http:// 或 https:// 开头",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (tempApiUrl.startsWith("http")) {
                            scope.launch {
                                viewModel.setApiUrl(tempApiUrl)
                            }
                            showApiUrlDialog = false
                        }
                    },
                    enabled = tempApiUrl.startsWith("http")
                ) {
                    Text("保存")
                }
            },
            dismissButton = {
                TextButton(onClick = { showApiUrlDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ) {
            Column(
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                content()
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { subtitle?.let { Text(it) } },
        leadingContent = { Icon(icon, contentDescription = null) },
        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}
