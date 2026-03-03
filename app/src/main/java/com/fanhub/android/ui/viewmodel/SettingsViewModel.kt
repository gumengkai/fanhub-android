package com.fanhub.android.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fanhub.android.BuildConfig
import com.fanhub.android.data.local.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsUiState(
    val apiUrl: String = ""
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()
    
    init {
        loadApiUrl()
    }
    
    private fun loadApiUrl() {
        viewModelScope.launch {
            settingsRepository.apiUrl.collect { url ->
                _uiState.update { it.copy(apiUrl = url) }
            }
        }
    }
    
    suspend fun setApiUrl(url: String) {
        settingsRepository.setApiUrl(url)
    }
    
    suspend fun resetApiUrl() {
        settingsRepository.resetApiUrl()
    }
}
