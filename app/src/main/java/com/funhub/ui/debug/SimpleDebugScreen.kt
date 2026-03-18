package com.funhub.ui.debug

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Simple in-memory log storage
object SimpleLog {
    private val logs = mutableListOf<String>()
    
    fun d(tag: String, message: String) {
        val entry = "${System.currentTimeMillis()} D/$tag: $message"
        synchronized(logs) {
            logs.add(entry)
            if (logs.size > 200) logs.removeAt(0)
        }
    }
    
    fun e(tag: String, message: String) {
        val entry = "${System.currentTimeMillis()} E/$tag: $message"
        synchronized(logs) {
            logs.add(entry)
            if (logs.size > 200) logs.removeAt(0)
        }
    }
    
    fun getLogs(): List<String> = synchronized(logs) { logs.toList() }
    
    fun clear() = synchronized(logs) { logs.clear() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDebugScreen(onNavigateBack: () -> Unit) {
    var logs by remember { mutableStateOf(SimpleLog.getLogs()) }
    
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
                    TextButton(onClick = { 
                        SimpleLog.clear()
                        logs = emptyList()
                    }) {
                        Text("清空")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("日志数量: ${logs.size}", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(8.dp))
            
            if (logs.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("暂无日志")
                }
            } else {
                val scrollState = rememberScrollState()
                LaunchedEffect(logs.size) {
                    scrollState.animateScrollTo(scrollState.maxValue)
                }
                
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    logs.forEach { log ->
                        Text(
                            text = log,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
