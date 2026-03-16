package com.funhub.ui.clips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funhub.domain.model.Result
import com.funhub.domain.usecase.CreateClipUseCase
import com.funhub.domain.usecase.GetClipInfoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ClipUiState(
    val videoId: String? = null,
    val videoTitle: String? = null,
    val videoUrl: String? = null,
    val videoDuration: Float? = null,
    val startTime: Float = 0f,
    val endTime: Float = 0f,
    val isLoading: Boolean = false,
    val isProcessing: Boolean = false,
    val processingMessage: String? = null,
    val clipResult: Pair<Boolean, String>? = null,
    val error: String? = null
)

@HiltViewModel
class ClipViewModel @Inject constructor(
    private val getClipInfoUseCase: GetClipInfoUseCase,
    private val createClipUseCase: CreateClipUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClipUiState())
    val uiState: StateFlow<ClipUiState> = _uiState.asStateFlow()

    fun loadVideoInfo(videoId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            when (val result = getClipInfoUseCase(videoId)) {
                is Result.Success -> {
                    val info = result.data
                    _uiState.update { 
                        it.copy(
                            videoId = videoId,
                            videoTitle = info.title,
                            videoUrl = null, // Will be set from stream URL
                            videoDuration = info.duration.toFloat(),
                            endTime = info.duration.toFloat(),
                            isLoading = false
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun updateStartTime(time: Float) {
        _uiState.update { state ->
            val newStartTime = time.coerceIn(0f, state.endTime)
            state.copy(startTime = newStartTime)
        }
    }

    fun updateEndTime(time: Float) {
        _uiState.update { state ->
            val maxDuration = state.videoDuration ?: time
            val newEndTime = time.coerceIn(state.startTime, maxDuration)
            state.copy(endTime = newEndTime)
        }
    }

    fun previewClip() {
        // TODO: Implement preview with selected time range
    }

    fun submitClip() {
        val videoId = _uiState.value.videoId ?: return
        val startTime = _uiState.value.startTime.toLong()
        val endTime = _uiState.value.endTime.toLong()
        
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isProcessing = true,
                    processingMessage = "正在提交剪辑任务..."
                )
            }
            
            when (val result = createClipUseCase(videoId, startTime, endTime)) {
                is Result.Success -> {
                    _uiState.update { 
                        it.copy(
                            isProcessing = false,
                            clipResult = Pair(true, "剪辑任务已提交，任务ID: ${result.data}")
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { 
                        it.copy(
                            isProcessing = false,
                            clipResult = Pair(false, result.message)
                        )
                    }
                }
                is Result.Loading -> {
                    _uiState.update { it.copy(isProcessing = true) }
                }
            }
        }
    }

    fun resetResult() {
        _uiState.update { it.copy(clipResult = null) }
    }
}
