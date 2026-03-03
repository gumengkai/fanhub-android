package com.fanhub.android.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.fanhub.android.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object PreferencesKeys {
    val API_URL = stringPreferencesKey("api_url")
}

@Singleton
class SettingsRepository @Inject constructor(
    private val context: Context
) {
    val apiUrl: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[PreferencesKeys.API_URL] ?: BuildConfig.DEFAULT_API_URL
    }
    
    suspend fun setApiUrl(url: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.API_URL] = url.trimEnd('/')
        }
    }
    
    suspend fun resetApiUrl() {
        context.dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.API_URL)
        }
    }
    
    fun getCurrentApiUrl(): String {
        // 注意：这是同步方法，仅用于初始值，推荐使用 Flow
        return BuildConfig.DEFAULT_API_URL
    }
}
