package com.funhub.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Server
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.funhub.domain.model.AppSettings
import com.funhub.domain.model.ThemeMode
import com.funhub.ui.components.LoadingIndicator

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settings by viewModel.settings.collectAsState()
    
    if (settings == null) {
        LoadingIndicator()
        return
    }
    
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Server Settings
        SettingsSection(
            title = "服务器设置",
            icon = Icons.Default.Server
        ) {
            ServerSettings(
                serverUrl = settings!!.serverAddress,
                onServerUrlChange = viewModel::updateServerUrl
            )
        }
        
        // Theme Settings
        SettingsSection(
            title = "主题设置",
            icon = Icons.Default.Palette
        ) {
            ThemeSettings(
                themeMode = settings!!.themeMode,
                dynamicColor = settings!!.useDynamicColor,
                onThemeModeChange = viewModel::updateThemeMode,
                onDynamicColorChange = viewModel::updateDynamicColor
            )
        }
        
        // About
        SettingsSection(
            title = "关于",
            icon = Icons.Default.Info
        ) {
            AboutSection()
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            
            content()
        }
    }
}

@Composable
fun ServerSettings(
    serverUrl: String,
    onServerUrlChange: (String) -> Unit
) {
    var text by remember(serverUrl) { mutableStateOf(serverUrl) }
    
    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("服务器地址") },
            placeholder = { Text("http://192.168.1.100:5000") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        TextButton(
            onClick = { onServerUrlChange(text) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("保存")
        }
    }
}

@Composable
fun ThemeSettings(
    themeMode: ThemeMode,
    dynamicColor: Boolean,
    onThemeModeChange: (ThemeMode) -> Unit,
    onDynamicColorChange: (Boolean) -> Unit
) {
    Column {
        // Theme Mode Selection
        Text(
            text = "主题模式",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        ThemeMode.values().forEach { mode ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeModeChange(mode) }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = themeMode == mode,
                    onClick = { onThemeModeChange(mode) }
                )
                Text(
                    text = when (mode) {
                        ThemeMode.LIGHT -> "浅色"
                        ThemeMode.DARK -> "深色"
                        ThemeMode.SYSTEM -> "跟随系统"
                    },
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
        
        // Dynamic Color Toggle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onDynamicColorChange(!dynamicColor) }
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("动态主题色 (Android 12+)")
            Switch(
                checked = dynamicColor,
                onCheckedChange = onDynamicColorChange
            )
        }
    }
}

@Composable
fun AboutSection() {
    Column {
        Text(
            text = "FunHub",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "版本 1.0.0",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "轻量级私人媒体中心客户端",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}