package com.fanhub.android.data.repository

import com.fanhub.android.data.model.*
import com.fanhub.android.data.remote.ApiService
import com.fanhub.android.data.remote.WatchHistoryRequest
import com.fanhub.android.data.remote.WatchHistoryListResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val message: String, val code: Int? = null) : Result<Nothing>()
    object Loading : Result<Nothing>()
}

@Singleton
class FanHubRepository @Inject constructor(
    private val apiService: ApiService
) {
    
    // ========== 视频相关 ==========
    
    fun getVideos(
        page: Int = 1,
        perPage: Int = 20,
        search: String? = null,
        sortBy: String? = null,
        order: String? = null,
        sourceId: Int? = null,
        tagId: Int? = null,
        favorite: Boolean? = null
    ): Flow<Result<VideoListResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getVideos(page, perPage, search, sortBy, order, sourceId, tagId, favorite)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
                    ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}", response.code()))
            }
        } catch (e: Exception) {
            emit(Result.Error("Network error: ${e.message}"))
        }
    }
    
    fun getVideoById(videoId: Int): Flow<Result<Video>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getVideoById(videoId)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
                    ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}", response.code()))
            }
        } catch (e: Exception) {
            emit(Result.Error("Network error: ${e.message}"))
        }
    }
    
    suspend fun toggleVideoFavorite(videoId: Int): Result<Unit> {
        return try {
            val response = apiService.toggleVideoFavorite(videoId)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Error: ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }
    
    // 视频流 URL 拼接
    fun getVideoStreamUrl(videoId: Int): String {
        return "/api/videos/$videoId/stream"
    }
    
    fun getVideoThumbnailUrl(videoId: Int, thumbnailPath: String?): String {
        return thumbnailPath ?: "/api/videos/$videoId/thumbnail"
    }
    
    suspend fun updateWatchHistory(videoId: Int, progress: Float, isCompleted: Boolean): Result<Unit> {
        return try {
            val response = apiService.updateWatchHistory(videoId, WatchHistoryRequest(progress, isCompleted))
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Error: ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }
    
    fun getRelatedVideos(videoId: Int, limit: Int = 10): Flow<Result<VideoListResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getRelatedVideos(videoId, limit)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
                    ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}", response.code()))
            }
        } catch (e: Exception) {
            emit(Result.Error("Network error: ${e.message}"))
        }
    }
    
    // ========== 图片相关 ==========
    
    fun getImages(
        page: Int = 1,
        perPage: Int = 20,
        search: String? = null,
        favorite: Boolean? = null
    ): Flow<Result<ImageListResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getImages(page, perPage, search, favorite)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
                    ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}", response.code()))
            }
        } catch (e: Exception) {
            emit(Result.Error("Network error: ${e.message}"))
        }
    }
    
    fun getAllImages(perPage: Int = 500): Flow<Result<ImageListResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getAllImages(perPage)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
                    ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}", response.code()))
            }
        } catch (e: Exception) {
            emit(Result.Error("Network error: ${e.message}"))
        }
    }
    
    suspend fun toggleImageFavorite(imageId: Int): Result<Unit> {
        return try {
            val response = apiService.toggleImageFavorite(imageId)
            if (response.isSuccessful) {
                Result.Success(Unit)
            } else {
                Result.Error("Error: ${response.code()}", response.code())
            }
        } catch (e: Exception) {
            Result.Error("Network error: ${e.message}")
        }
    }
    
    // 图片 URL 在运行时拼接
    // fun getImageFileUrl(imageId: Int): String
    // fun getImageThumbnailUrl(imageId: Int, thumbnailPath: String?): String
    
    // ========== 标签相关 ==========
    
    fun getTags(): Flow<Result<List<Tag>>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getTags()
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
                    ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}", response.code()))
            }
        } catch (e: Exception) {
            emit(Result.Error("Network error: ${e.message}"))
        }
    }
    
    // ========== 收藏相关 ==========
    
    fun getFavorites(
        page: Int = 1,
        perPage: Int = 20,
        type: String? = null
    ): Flow<Result<FavoriteListResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getFavorites(page, perPage, type)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
                    ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}", response.code()))
            }
        } catch (e: Exception) {
            emit(Result.Error("Network error: ${e.message}"))
        }
    }
    
    fun getFavoriteStats(): Flow<Result<FavoriteStats>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getFavoriteStats()
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
                    ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}", response.code()))
            }
        } catch (e: Exception) {
            emit(Result.Error("Network error: ${e.message}"))
        }
    }
    
    // ========== 历史相关 ==========
    
    fun getWatchHistory(
        page: Int = 1,
        perPage: Int = 20
    ): Flow<Result<WatchHistoryListResponse>> = flow {
        emit(Result.Loading)
        try {
            val response = apiService.getWatchHistory(page, perPage)
            if (response.isSuccessful) {
                response.body()?.let { emit(Result.Success(it)) }
                    ?: emit(Result.Error("Empty response"))
            } else {
                emit(Result.Error("Error: ${response.code()}", response.code()))
            }
        } catch (e: Exception) {
            emit(Result.Error("Network error: ${e.message}"))
        }
    }
}
