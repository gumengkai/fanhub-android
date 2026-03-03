package com.fanhub.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fanhub.android.data.model.VideoListResponse
import com.fanhub.android.data.repository.FanHubRepository
import com.fanhub.android.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ShortVideoUiState(
    val videos: Result<VideoListResponse> = Result.Loading
)

@HiltViewModel
class ShortVideoViewModel @Inject constructor(
    private val repository: FanHubRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ShortVideoUiState())
    val uiState: StateFlow<ShortVideoUiState> = _uiState.asStateFlow()
    
    init {
        loadVideos()
    }
    
    fun loadVideos() {
        viewModelScope.launch {
            repository.getVideos(
                page = 1,
                perPage = 50
            ).collect { result ->
                _uiState.update { it.copy(videos = result) }
            }
        }
    }
    
    fun toggleFavorite(videoId: Int) {
        viewModelScope.launch {
            repository.toggleVideoFavorite(videoId)
            loadVideos() // 重新加载以更新状态
        }
    }
}
