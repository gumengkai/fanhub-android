package com.funhub.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.funhub.domain.model.AppSettings
import com.funhub.domain.model.ThemeMode
import com.funhub.domain.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _settings = MutableStateFlow<AppSettings?>(null)
    val settings: StateFlow<AppSettings?> = _settings.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            settingsRepository.getSettings().collect { settings ->
                _settings.value = settings ?: AppSettings(
                    serverAddress = "",
                    themeMode = ThemeMode.SYSTEM,
                    useDynamicColor = false
                )
            }
        }
    }

    fun updateServerUrl(url: String) {
        viewModelScope.launch {
            settingsRepository.saveServerAddress(url)
            // Refresh settings
            _settings.value = _settings.value?.copy(serverAddress = url)
                ?: AppSettings(serverAddress = url, themeMode = ThemeMode.SYSTEM, useDynamicColor = true)
        }
    }

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            settingsRepository.saveThemeMode(mode)
            _settings.value = _settings.value?.copy(themeMode = mode)
                ?: AppSettings(serverAddress = "", themeMode = mode, useDynamicColor = true)
        }
    }

    fun updateDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveDynamicColor(enabled)
            _settings.value = _settings.value?.copy(useDynamicColor = enabled)
                ?: AppSettings(serverAddress = "", themeMode = ThemeMode.SYSTEM, useDynamicColor = enabled)
        }
    }
}