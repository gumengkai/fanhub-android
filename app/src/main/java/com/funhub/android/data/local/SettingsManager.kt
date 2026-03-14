package com.funhub.android.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import com.funhub.android.util.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 应用设置管理器
 * 
 * 使用 DataStore 存储用户配置
 * - 后端 API 地址
 * - 深色模式设置
 * - 播放器设置
 * - 其他用户偏好
 */
@Singleton
class SettingsManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    companion object {
        val API_URL_KEY = stringPreferencesKey(Constants.DataStoreKeys.API_URL)
        val DARK_MODE_KEY = booleanPreferencesKey(Constants.DataStoreKeys.DARK_MODE)
        val AUTO_PLAY_KEY = booleanPreferencesKey(Constants.DataStoreKeys.AUTO_PLAY)
        val SLIDESHOW_INTERVAL_KEY = intPreferencesKey(Constants.DataStoreKeys.SLIDESHOW_INTERVAL)
        val SHORT_VIDEO_MODE_KEY = stringPreferencesKey(Constants.DataStoreKeys.SHORT_VIDEO_MODE)
    }

    // ==================== API URL ====================

    fun getApiUrlFlow(): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[API_URL_KEY]?.takeIf { it.isNotBlank() }
                ?: Constants.DEFAULT_API_URL
        }

    suspend fun setApiUrl(url: String) {
        dataStore.edit { preferences ->
            preferences[API_URL_KEY] = url.trim().removeSuffix("/")
        }
    }

    fun getApiUrl(): String {
        // 注意：这是阻塞方法，建议在协程中使用 getApiUrlFlow()
        return Constants.DEFAULT_API_URL
    }

    // ==================== 深色模式 ====================

    fun getDarkModeFlow(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    // ==================== 自动播放 ====================

    fun getAutoPlayFlow(): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[AUTO_PLAY_KEY] ?: true
        }

    suspend fun setAutoPlay(enabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[AUTO_PLAY_KEY] = enabled
        }
    }

    // ==================== 幻灯片间隔 ====================

    fun getSlideshowIntervalFlow(): Flow<Int> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[SLIDESHOW_INTERVAL_KEY]
                ?: Constants.SLIDESHOW_DEFAULT_INTERVAL_SECONDS
        }

    suspend fun setSlideshowInterval(seconds: Int) {
        dataStore.edit { preferences ->
            preferences[SLIDESHOW_INTERVAL_KEY] = seconds.coerceIn(
                Constants.SLIDESHOW_MIN_INTERVAL_SECONDS,
                Constants.SLIDESHOW_MAX_INTERVAL_SECONDS
            )
        }
    }

    // ==================== 短视频模式 ====================

    fun getShortVideoModeFlow(): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[SHORT_VIDEO_MODE_KEY] ?: "sequential"
        }

    suspend fun setShortVideoMode(mode: String) {
        dataStore.edit { preferences ->
            preferences[SHORT_VIDEO_MODE_KEY] = if (mode == "random") "random" else "sequential"
        }
    }

    // ==================== 清除所有设置 ====================

    suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
