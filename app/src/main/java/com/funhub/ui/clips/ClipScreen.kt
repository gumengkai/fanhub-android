package com.funhub.ui.clips

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.funhub.ui.components.ErrorView
import com.funhub.ui.components.LoadingIndicator
import com.funhub.ui.components.VideoPlayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClipScreen(
    videoId: String? = null,
    viewModel: ClipViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    // Load video info when screen opens
    videoId?.let { viewModel.loadVideoInfo(it) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("视频剪辑") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> LoadingIndicator()
                uiState.error != null -> ErrorView(
                    message = uiState.error!!,
                    onRetry = { videoId?.let { viewModel.loadVideoInfo(it) } }
                )
                uiState.isProcessing -> ProcessingView(message = uiState.processingMessage)
                uiState.clipResult != null -> ClipResultView(
                    success = uiState.clipResult!!.first,
                    message = uiState.clipResult!!.second,
                    onDismiss = viewModel::resetResult
                )
                else -> {
                    ClipContent(
                        uiState = uiState,
                        onStartTimeChange = viewModel::updateStartTime,
                        onEndTimeChange = viewModel::updateEndTime,
                        onPreview = viewModel::previewClip,
                        onSubmit = viewModel::submitClip
                    )
                }
            }
        }
    }
}

@Composable
fun ClipContent(
    uiState: ClipUiState,
    onStartTimeChange: (Float) -> Unit,
    onEndTimeChange: (Float) -> Unit,
    onPreview: () -> Unit,
    onSubmit: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Video Preview
        if (uiState.videoUrl != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    VideoPlayer(
                        videoUrl = uiState.videoUrl,
                        modifier = Modifier.fillMaxSize(),
                        autoPlay = false
                    )
                }
            }
        }
        
        // Video Title
        uiState.videoTitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleLarge
            )
        }
        
        // Duration Info
        uiState.videoDuration?.let { duration ->
            Text(
                text = "总时长: ${formatDuration(duration)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Start Time Slider
        Text(
            text = "开始时间: ${formatDuration(uiState.startTime.toLong())}",
            style = MaterialTheme.typography.bodyLarge
        )
        Slider(
            value = uiState.startTime,
            onValueChange = onStartTimeChange,
            valueRange = 0f..(uiState.videoDuration ?: 0f),
            modifier = Modifier.fillMaxWidth()
        )
        
        // End Time Slider
        Text(
            text = "结束时间: ${formatDuration(uiState.endTime.toLong())}",
            style = MaterialTheme.typography.bodyLarge
        )
        Slider(
            value = uiState.endTime,
            onValueChange = onEndTimeChange,
            valueRange = uiState.startTime..(uiState.videoDuration ?: 0f),
            modifier = Modifier.fillMaxWidth()
        )
        
        // Selected Duration
        Text(
            text = "选中时长: ${formatDuration((uiState.endTime - uiState.startTime).toLong())}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Action Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onPreview,
                modifier = Modifier.weight(1f),
                enabled = uiState.videoUrl != null
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("预览")
            }
            
            Button(
                onClick = onSubmit,
                modifier = Modifier.weight(1f),
                enabled = uiState.videoId != null && uiState.endTime > uiState.startTime
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCut,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text("提交剪辑")
            }
        }
    }
}

@Composable
fun ProcessingView(message: String?) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = message ?: "处理中...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ClipResultView(
    success: Boolean,
    message: String,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = if (success) Icons.Default.ContentCut else Icons.Default.ContentCut,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = if (success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Text(
                    text = if (success) "剪辑成功" else "剪辑失败",
                    style = MaterialTheme.typography.headlineSmall,
                    color = if (success) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Button(onClick = onDismiss) {
                    Text("确定")
                }
            }
        }
    }
}

private fun formatDuration(durationMs: Long): String {
    val seconds = durationMs / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    return if (hours > 0) {
        String.format("%02d:%02d:%02d", hours, minutes % 60, seconds % 60)
    } else {
        String.format("%02d:%02d", minutes, seconds % 60)
    }
}
