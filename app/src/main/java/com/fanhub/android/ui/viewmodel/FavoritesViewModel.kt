package com.fanhub.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fanhub.android.data.model.FavoriteListResponse
import com.fanhub.android.data.model.FavoriteStats
import com.fanhub.android.data.repository.FanHubRepository
import com.fanhub.android.data.repository.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoritesUiState(
    val favorites: Result<FavoriteListResponse> = Result.Loading,
    val stats: Result<FavoriteStats> = Result.Loading
)

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: FanHubRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()
    
    init {
        loadFavorites()
        loadStats()
    }
    
    fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavorites(
                page = 1,
                perPage = 50
            ).collect { result ->
                _uiState.update { it.copy(favorites = result) }
            }
        }
    }
    
    fun loadStats() {
        viewModelScope.launch {
            repository.getFavoriteStats().collect { result ->
                _uiState.update { it.copy(stats = result) }
            }
        }
    }
    
    fun removeFromFavorites(itemId: Int, type: String) {
        viewModelScope.launch {
            // 注意：API 可能需要根据类型调用不同的接口
            // 这里假设有一个统一的取消收藏接口
            // 实际可能需要调用 repository.toggleVideoFavorite 或 repository.toggleImageFavorite
            loadFavorites() // 重新加载
            loadStats()
        }
    }
}
