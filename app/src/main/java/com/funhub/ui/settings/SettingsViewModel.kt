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

    private val _settings = MutableStateFlow<AppSettings>(
        AppSettings(
            serverAddress = "",
            themeMode = ThemeMode.SYSTEM,
            useDynamicColor = false
        )
    )
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            try {
                settingsRepository.getSettings().collect { settings ->
                    _settings.value = settings
                }
            } catch (e: Exception) {
                // Keep default value on error
                _settings.value = AppSettings(
                    serverAddress = "",
                    themeMode = ThemeMode.SYSTEM,
                    useDynamicColor = false
                )
            }
        }
    }

    fun updateServerUrl(url: String) {
        viewModelScope.launch {
            try {
                settingsRepository.saveServerAddress(url)
                _settings.value = _settings.value.copy(serverAddress = url)
            } catch (e: Exception) {
                // Ignore save errors
            }
        }
    }

    fun updateThemeMode(mode: ThemeMode) {
        viewModelScope.launch {
            try {
                settingsRepository.saveThemeMode(mode)
                _settings.value = _settings.value.copy(themeMode = mode)
            } catch (e: Exception) {
                // Ignore save errors
            }
        }
    }

    fun updateDynamicColor(enabled: Boolean) {
        viewModelScope.launch {
            try {
                settingsRepository.saveDynamicColor(enabled)
                _settings.value = _settings.value.copy(useDynamicColor = enabled)
            } catch (e: Exception) {
                // Ignore save errors
            }
        }
    }
}