package com.fanhub.android.data.remote

import com.fanhub.android.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    
    // ========== 视频 API ==========
    
    /**
     * 获取视频列表
     * @param page 页码
     * @param perPage 每页数量
     * @param search 搜索关键词
     * @param sortBy 排序字段 (created_at, title, duration, view_count, file_size)
     * @param order 排序方向 (asc, desc)
     * @param sourceId 来源 ID
     * @param tagId 标签 ID
     * @param favorite 是否只看收藏
     */
    @GET("videos")
    suspend fun getVideos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("search") search: String? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("order") order: String? = null,
        @Query("source_id") sourceId: Int? = null,
        @Query("tag_id") tagId: Int? = null,
        @Query("favorite") favorite: Boolean? = null
    ): Response<VideoListResponse>
    
    /**
     * 获取视频详情
     */
    @GET("videos/{id}")
    suspend fun getVideoById(@Path("id") videoId: Int): Response<Video>
    
    /**
     * 切换收藏状态
     */
    @POST("videos/{id}/favorite")
    suspend fun toggleVideoFavorite(@Path("id") videoId: Int): Response<Unit>
    
    // 注意：视频流和缩略图 URL 在 Repository 中拼接
    // fun getVideoStreamUrl(videoId: Int): String
    // fun getVideoThumbnailUrl(videoId: Int, thumbnailPath: String?): String
    
    /**
     * 更新观看历史
     */
    @POST("history/video/{videoId}")
    suspend fun updateWatchHistory(
        @Path("videoId") videoId: Int,
        @Body history: WatchHistoryRequest
    ): Response<Unit>
    
    /**
     * 获取相关视频
     */
    @GET("videos/{id}/related")
    suspend fun getRelatedVideos(
        @Path("id") videoId: Int,
        @Query("limit") limit: Int = 10
    ): Response<VideoListResponse>
    
    // ========== 图片 API ==========
    
    /**
     * 获取图片列表
     */
    @GET("images")
    suspend fun getImages(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("search") search: String? = null,
        @Query("favorite") favorite: Boolean? = null
    ): Response<ImageListResponse>
    
    /**
     * 获取所有图片（用于幻灯片）
     */
    @GET("images/all")
    suspend fun getAllImages(
        @Query("per_page") perPage: Int = 500
    ): Response<ImageListResponse>
    
    /**
     * 获取图片详情
     */
    @GET("images/{id}")
    suspend fun getImageById(@Path("id") imageId: Int): Response<Image>
    
    /**
     * 切换图片收藏状态
     */
    @POST("images/{id}/favorite")
    suspend fun toggleImageFavorite(@Path("id") imageId: Int): Response<Unit>
    
    // 注意：图片 URL 在 Repository 中拼接
    // fun getImageFileUrl(imageId: Int): String
    // fun getImageThumbnailUrl(imageId: Int, thumbnailPath: String?): String
    
    // ========== 标签 API ==========
    
    /**
     * 获取所有标签
     */
    @GET("tags")
    suspend fun getTags(): Response<List<Tag>>
    
    /**
     * 创建标签
     */
    @POST("tags")
    suspend fun createTag(@Body tag: TagCreateRequest): Response<Tag>
    
    /**
     * 为视频添加标签
     */
    @POST("videos/{id}/tags")
    suspend fun addVideoTag(
        @Path("id") videoId: Int,
        @Body request: TagIdRequest
    ): Response<Unit>
    
    /**
     * 移除视频标签
     */
    @DELETE("videos/{id}/tags/{tagId}")
    suspend fun removeVideoTag(
        @Path("id") videoId: Int,
        @Path("tagId") tagId: Int
    ): Response<Unit>
    
    // ========== 收藏 API ==========
    
    /**
     * 获取收藏列表
     */
    @GET("favorites")
    suspend fun getFavorites(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("type") type: String? = null // "video" or "image"
    ): Response<FavoriteListResponse>
    
    /**
     * 获取收藏统计
     */
    @GET("favorites/stats")
    suspend fun getFavoriteStats(): Response<FavoriteStats>
    
    // ========== 历史 API ==========
    
    /**
     * 获取观看历史
     */
    @GET("history")
    suspend fun getWatchHistory(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20
    ): Response<WatchHistoryListResponse>
    
    /**
     * 获取视频观看历史
     */
    @GET("history/video/{videoId}")
    suspend fun getVideoWatchHistory(@Path("videoId") videoId: Int): Response<WatchHistory?>
    
    /**
     * 清空观看历史
     */
    @POST("history/clear")
    suspend fun clearWatchHistory(): Response<Unit>
    
    /**
     * 获取历史统计
     */
    @GET("history/stats")
    suspend fun getHistoryStats(): Response<HistoryStats>
}

// ========== 请求体数据类 ==========

data class WatchHistoryRequest(
    @SerializedName("progress") val progress: Float,
    @SerializedName("is_completed") val isCompleted: Boolean
)

data class TagCreateRequest(
    @SerializedName("name") val name: String,
    @SerializedName("color") val color: String
)

data class TagIdRequest(
    @SerializedName("tag_id") val tagId: Int
)

data class WatchHistory(
    @SerializedName("id") val id: Int,
    @SerializedName("video_id") val videoId: Int,
    @SerializedName("progress") val progress: Float,
    @SerializedName("is_completed") val isCompleted: Boolean,
    @SerializedName("watched_at") val watchedAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class WatchHistoryListResponse(
    @SerializedName("items") val items: List<WatchHistory>,
    @SerializedName("total") val total: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("per_page") val perPage: Int
)

data class HistoryStats(
    @SerializedName("total_watched") val totalWatched: Int,
    @SerializedName("total_progress") val totalProgress: Int,
    @SerializedName("total_completed") val totalCompleted: Int
)
