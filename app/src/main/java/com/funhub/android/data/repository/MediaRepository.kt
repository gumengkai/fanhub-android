package com.funhub.android.data.repository

import com.funhub.android.data.api.FunHubApi
import com.funhub.android.data.api.models.*
import com.funhub.android.data.local.SettingsManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 媒体数据仓库
 * 
 * 统一管理视频、图片、历史等数据的获取和缓存
 */
@Singleton
class MediaRepository @Inject constructor(
    private val api: FunHubApi,
    private val settingsManager: SettingsManager
) {

    // ==================== 视频相关 ====================

    suspend fun getVideos(
        page: Int = 1,
        pageSize: Int = 20,
        search: String? = null,
        sortBy: String? = null,
        sortOrder: String? = null,
        favorite: Boolean? = null,
        tagId: Int? = null
    ): Result<VideoListResponse> {
        return try {
            val response = api.getVideos(page, pageSize, search, sortBy, sortOrder, favorite, tagId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get videos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVideo(videoId: Int): Result<Video> {
        return try {
            val response = api.getVideo(videoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get video: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVideoStreamUrl(videoId: Int): Result<String> {
        return try {
            val apiUrl = settingsManager.getApiUrlFlow().value
            val streamUrl = "$apiUrl/videos/$videoId/stream"
            Result.success(streamUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleVideoFavorite(videoId: Int): Result<Video> {
        return try {
            val response = api.toggleVideoFavorite(videoId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to toggle favorite: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRelatedVideos(videoId: Int): Result<List<Video>> {
        return try {
            val response = api.getRelatedVideos(videoId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get related videos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== 图片相关 ====================

    suspend fun getImages(
        page: Int = 1,
        pageSize: Int = 20,
        search: String? = null,
        favorite: Boolean? = null
    ): Result<ImageListResponse> {
        return try {
            val response = api.getImages(page, pageSize, search, favorite)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get images: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllImages(): Result<List<Image>> {
        return try {
            val response = api.getAllImages()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get all images: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getImageFileUrl(imageId: Int): Result<String> {
        return try {
            val apiUrl = settingsManager.getApiUrlFlow().value
            val fileUrl = "$apiUrl/images/$imageId/file"
            Result.success(fileUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleImageFavorite(imageId: Int): Result<Image> {
        return try {
            val response = api.toggleImageFavorite(imageId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to toggle favorite: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== 播放历史 ====================

    suspend fun getHistory(page: Int = 1, pageSize: Int = 20): Result<List<WatchHistory>> {
        return try {
            val response = api.getHistory(page, pageSize)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get history: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getVideoHistory(videoId: Int): Result<WatchHistory?> {
        return try {
            val response = api.getVideoHistory(videoId)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                Result.failure(Exception("Failed to get video history: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateVideoHistory(
        videoId: Int,
        playbackPosition: Int,
        isCompleted: Boolean = false
    ): Result<WatchHistory> {
        return try {
            val body = mapOf(
                "playback_position" to playbackPosition,
                "is_completed" to isCompleted
            )
            val response = api.updateVideoHistory(videoId, body)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to update history: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteVideoHistory(videoId: Int): Result<Unit> {
        return try {
            val response = api.deleteVideoHistory(videoId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete history: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHistoryStats(): Result<HistoryStats> {
        return try {
            val response = api.getHistoryStats()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get history stats: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== 收藏 ====================

    suspend fun getFavorites(): Result<FavoritesResponse> {
        return try {
            val response = api.getFavorites()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get favorites: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== 标签 ====================

    suspend fun getTags(): Result<List<Tag>> {
        return try {
            val response = api.getTags()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception("Failed to get tags: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ==================== 健康检查 ====================

    suspend fun healthCheck(): Result<Boolean> {
        return try {
            val response = api.healthCheck()
            Result.success(response.isSuccessful)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
