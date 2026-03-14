package com.funhub.android.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funhub.android.data.local.SettingsManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 设置页面 ViewModel
 */
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsManager: SettingsManager
) : ViewModel() {

    data class SettingsState(
        val apiUrl: String = "",
        val darkMode: Boolean = false,
        val autoPlay: Boolean = true,
        val slideshowInterval: Int = 5,
        val shortVideoMode: String = "sequential"
    )

    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    /**
     * 加载设置
     */
    fun loadSettings() {
        viewModelScope.launch {
            settingsManager.getApiUrlFlow().collect { url ->
                _settingsState.value = _settingsState.value.copy(apiUrl = url)
            }
        }

        viewModelScope.launch {
            settingsManager.getDarkModeFlow().collect { enabled ->
                _settingsState.value = _settingsState.value.copy(darkMode = enabled)
            }
        }

        viewModelScope.launch {
            settingsManager.getAutoPlayFlow().collect { enabled ->
                _settingsState.value = _settingsState.value.copy(autoPlay = enabled)
            }
        }
    }

    /**
     * 保存 API 地址
     */
    suspend fun saveApiUrl(url: String) {
        settingsManager.setApiUrl(url)
    }

    /**
     * 保存深色模式设置
     */
    suspend fun saveDarkMode(enabled: Boolean) {
        settingsManager.setDarkMode(enabled)
    }

    /**
     * 保存自动播放设置
     */
    suspend fun saveAutoPlay(enabled: Boolean) {
        settingsManager.setAutoPlay(enabled)
    }
}
