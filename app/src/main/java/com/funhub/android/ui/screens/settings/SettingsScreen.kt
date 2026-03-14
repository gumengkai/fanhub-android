package com.funhub.android.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.funhub.android.data.local.SettingsManager
import kotlinx.coroutines.launch

/**
 * 设置页面
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    
    var apiUrl by remember { mutableStateOf("") }
    var apiUrlEdited by remember { mutableStateOf("") }
    var darkMode by remember { mutableStateOf(false) }
    var autoPlay by remember { mutableStateOf(true) }

    // 加载设置
    LaunchedEffect(Unit) {
        viewModel.loadSettings()
    }

    // 监听设置变化
    LaunchedEffect(viewModel.settingsState) {
        viewModel.settingsState.collect { state ->
            apiUrl = state.apiUrl
            apiUrlEdited = state.apiUrl
            darkMode = state.darkMode
            autoPlay = state.autoPlay
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
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
            // 服务器设置
            SettingsSection(title = "服务器设置") {
                OutlinedTextField(
                    value = apiUrlEdited,
                    onValueChange = { apiUrlEdited = it },
                    label = { Text("后端 API 地址") },
                    placeholder = { Text("例如：http://192.168.1.100:5000/api") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = { apiUrlEdited = apiUrl }
                    ) {
                        Text("重置")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                viewModel.saveApiUrl(apiUrlEdited)
                            }
                        },
                        enabled = apiUrlEdited != apiUrl && apiUrlEdited.isNotBlank()
                    ) {
                        Text("保存")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "提示：Android 模拟器使用 http://10.0.2.2:5000/api 访问本地主机",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // 播放设置
            SettingsSection(title = "播放设置") {
                SettingsSwitch(
                    title = "自动播放",
                    subtitle = "进入视频后自动开始播放",
                    checked = autoPlay,
                    onCheckedChange = { checked ->
                        autoPlay = checked
                        scope.launch {
                            viewModel.saveAutoPlay(checked)
                        }
                    }
                )
            }

            // 外观设置
            SettingsSection(title = "外观设置") {
                SettingsSwitch(
                    title = "深色模式",
                    subtitle = "使用深色主题",
                    checked = darkMode,
                    onCheckedChange = { checked ->
                        darkMode = checked
                        scope.launch {
                            viewModel.saveDarkMode(checked)
                        }
                    }
                )
            }

            // 关于
            SettingsSection(title = "关于") {
                ListItem(
                    headlineContent = { Text("版本") },
                    supportingContent = { Text("1.0.0") }
                )

                ListItem(
                    headlineContent = { Text("FunHub") },
                    supportingContent = { Text("让本地媒体管理更有趣") }
                )
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        content()
    }
}

@Composable
fun SettingsSwitch(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(subtitle) },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    )
}
