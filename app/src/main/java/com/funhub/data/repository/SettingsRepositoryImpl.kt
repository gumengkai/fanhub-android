package com.funhub.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.funhub.domain.model.AppSettings
import com.funhub.domain.model.ThemeMode
import com.funhub.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {

    private val dataStore = context.dataStore

    companion object {
        private val SERVER_ADDRESS = stringPreferencesKey("server_address")
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
        
        private const val DEFAULT_SERVER = "http://192.168.1.100:5000"
    }

    override fun getSettings(): Flow<AppSettings> {
        return dataStore.data.map { preferences ->
            AppSettings(
                serverAddress = preferences[SERVER_ADDRESS] ?: DEFAULT_SERVER,
                themeMode = ThemeMode.valueOf(preferences[THEME_MODE] ?: ThemeMode.SYSTEM.name),
                useDynamicColor = preferences[DYNAMIC_COLOR] ?: true
            )
        }
    }

    override suspend fun saveServerAddress(address: String) {
        dataStore.edit { preferences ->
            preferences[SERVER_ADDRESS] = address
        }
    }

    override suspend fun saveThemeMode(mode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = mode.name
        }
    }

    override suspend fun saveDynamicColor(useDynamic: Boolean) {
        dataStore.edit { preferences ->
            preferences[DYNAMIC_COLOR] = useDynamic
        }
    }

    override suspend fun getServerAddress(): String {
        return dataStore.data.map { it[SERVER_ADDRESS] ?: DEFAULT_SERVER }.first()
    }
}
