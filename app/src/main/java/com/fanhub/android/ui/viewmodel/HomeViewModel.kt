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
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val recentVideos: Result<VideoListResponse> = Result.Loading,
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: FanHubRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadRecentVideos()
    }
    
    private fun loadRecentVideos() {
        viewModelScope.launch {
            repository.getVideos(
                page = 1,
                perPage = 10,
                sortBy = "created_at",
                order = "desc"
            ).collect { result ->
                _uiState.value = _uiState.value.copy(recentVideos = result)
            }
        }
    }
}
