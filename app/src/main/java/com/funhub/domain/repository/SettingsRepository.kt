package com.funhub.domain.repository

import com.funhub.domain.model.AppSettings
import com.funhub.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<AppSettings>
    suspend fun saveServerAddress(address: String)
    suspend fun saveThemeMode(mode: ThemeMode)
    suspend fun saveDynamicColor(useDynamic: Boolean)
    suspend fun getServerAddress(): String
}
