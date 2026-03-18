package com.funhub.ui.debug

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

// Global log storage
object AppLog {
    private val logs = mutableListOf<LogEntry>()
    private val listeners = mutableListOf<() -> Unit>()
    
    data class LogEntry(
        val timestamp: Long,
        val level: LogLevel,
        val tag: String,
        val message: String
    )
    
    enum class LogLevel { DEBUG, INFO, WARN, ERROR }
    
    fun d(tag: String, message: String) = addLog(LogLevel.DEBUG, tag, message)
    fun i(tag: String, message: String) = addLog(LogLevel.INFO, tag, message)
    fun w(tag: String, message: String) = addLog(LogLevel.WARN, tag, message)
    fun e(tag: String, message: String) = addLog(LogLevel.ERROR, tag, message)
    
    private fun addLog(level: LogLevel, tag: String, message: String) {
        synchronized(logs) {
            logs.add(LogEntry(System.currentTimeMillis(), level, tag, message))
            // Keep only last 500 logs
            if (logs.size > 500) {
                logs.removeAt(0)
            }
        }
        // Copy listeners to avoid concurrent modification
        val listenersCopy = synchronized(listeners) { listeners.toList() }
        listenersCopy.forEach { it() }
    }
    
    fun getLogs(): List<LogEntry> = synchronized(logs) { logs.toList() }
    
    fun clear() {
        synchronized(logs) { logs.clear() }
        val listenersCopy = synchronized(listeners) { listeners.toList() }
        listenersCopy.forEach { it() }
    }
    
    fun addListener(listener: () -> Unit) {
        listeners.add(listener)
    }
    
    fun removeListener(listener: () -> Unit) {
        listeners.remove(listener)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DebugLogScreen(
    onNavigateBack: () -> Unit
) {
    var logs by remember { mutableStateOf(AppLog.getLogs()) }
    val listState = rememberLazyListState()
    
    // Auto-scroll to bottom
    LaunchedEffect(logs.size) {
        if (logs.isNotEmpty()) {
            listState.animateScrollToItem(logs.size - 1)
        }
    }
    
    // Listen for new logs
    DisposableEffect(Unit) {
        val listener = { logs = AppLog.getLogs() }
        AppLog.addListener(listener)
        onDispose { AppLog.removeListener(listener) }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("调试日志") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("返回")
                    }
                },
                actions = {
                    IconButton(onClick = { logs = AppLog.getLogs() }) {
                        Icon(Icons.Default.Refresh, "刷新")
                    }
                    IconButton(onClick = { AppLog.clear() }) {
                        Icon(Icons.Default.Clear, "清空")
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
            // Stats
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val errorCount = logs.count { it.level == AppLog.LogLevel.ERROR }
                val warnCount = logs.count { it.level == AppLog.LogLevel.WARN }
                StatItem("日志数", logs.size.toString())
                StatItem("错误", errorCount.toString(), MaterialTheme.colorScheme.error)
                StatItem("警告", warnCount.toString(), MaterialTheme.colorScheme.tertiary)
            }
            
            Divider()
            
            // Log list
            if (logs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("暂无日志", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(logs, key = { it.timestamp + it.message.hashCode() }) { log ->
                        LogItem(log)
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.primary) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun LogItem(log: AppLog.LogEntry) {
    val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
    val color = when (log.level) {
        AppLog.LogLevel.DEBUG -> MaterialTheme.colorScheme.onSurfaceVariant
        AppLog.LogLevel.INFO -> MaterialTheme.colorScheme.primary
        AppLog.LogLevel.WARN -> MaterialTheme.colorScheme.tertiary
        AppLog.LogLevel.ERROR -> MaterialTheme.colorScheme.error
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = when (log.level) {
                AppLog.LogLevel.ERROR -> MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f)
                AppLog.LogLevel.WARN -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
                else -> MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row {
                Text(
                    text = timeFormat.format(Date(log.timestamp)),
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = log.level.name,
                    fontSize = 10.sp,
                    color = color,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = log.tag,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily.Monospace
                )
            }
            Text(
                text = log.message,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
