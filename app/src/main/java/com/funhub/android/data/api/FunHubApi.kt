package com.funhub.android.data.api

import com.funhub.android.data.api.models.*
import retrofit2.Response
import retrofit2.http.*

/**
 * FunHub API 服务接口
 * 
 * 定义所有与后端交互的 API 端点
 */
interface FunHubApi {

    // ==================== 视频 API ====================

    /**
     * 获取视频列表
     * @param page 页码
     * @param pageSize 每页数量
     * @param search 搜索关键词
     * @param sortBy 排序字段 (created_at, title, view_count)
     * @param sortOrder 排序顺序 (asc, desc)
     * @param favorite 是否只显示收藏
     * @param tagId 标签 ID 筛选
     */
    @GET("videos")
    suspend fun getVideos(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("search") search: String? = null,
        @Query("sort_by") sortBy: String? = null,
        @Query("sort_order") sortOrder: String? = null,
        @Query("favorite") favorite: Boolean? = null,
        @Query("tag_id") tagId: Int? = null
    ): Response<VideoListResponse>

    /**
     * 获取视频详情
     */
    @GET("videos/{id}")
    suspend fun getVideo(@Path("id") videoId: Int): Response<Video>

    /**
     * 获取视频流 URL
     */
    @GET("videos/{id}/stream")
    suspend fun getVideoStreamUrl(@Path("id") videoId: Int): Response<Unit>

    /**
     * 更新视频信息（标题、描述、收藏状态）
     */
    @PUT("videos/{id}")
    suspend fun updateVideo(
        @Path("id") videoId: Int,
        @Body body: Map<String, Any>
    ): Response<Video>

    /**
     * 收藏/取消收藏视频
     */
    @POST("videos/{id}/favorite")
    suspend fun toggleVideoFavorite(@Path("id") videoId: Int): Response<Video>

    /**
     * 获取相关视频
     */
    @GET("videos/{id}/related")
    suspend fun getRelatedVideos(@Path("id") videoId: Int): Response<List<Video>>

    /**
     * 获取视频标签
     */
    @GET("videos/{id}/tags")
    suspend fun getVideoTags(@Path("id") videoId: Int): Response<List<Tag>>

    /**
     * 添加视频标签
     */
    @POST("videos/{id}/tags")
    suspend fun addVideoTag(
        @Path("id") videoId: Int,
        @Body body: Map<String, Int>
    ): Response<List<Tag>>

    /**
     * 移除视频标签
     */
    @DELETE("videos/{id}/tags/{tagId}")
    suspend fun removeVideoTag(
        @Path("id") videoId: Int,
        @Path("tagId") tagId: Int
    ): Response<Unit>

    // ==================== 图片 API ====================

    /**
     * 获取图片列表
     */
    @GET("images")
    suspend fun getImages(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20,
        @Query("search") search: String? = null,
        @Query("favorite") favorite: Boolean? = null
    ): Response<ImageListResponse>

    /**
     * 获取所有图片（用于幻灯片）
     */
    @GET("images/all")
    suspend fun getAllImages(): Response<List<Image>>

    /**
     * 获取图片详情
     */
    @GET("images/{id}")
    suspend fun getImage(@Path("id") imageId: Int): Response<Image>

    /**
     * 获取原图 URL
     */
    @GET("images/{id}/file")
    suspend fun getImageFileUrl(@Path("id") imageId: Int): Response<Unit>

    /**
     * 收藏/取消收藏图片
     */
    @POST("images/{id}/favorite")
    suspend fun toggleImageFavorite(@Path("id") imageId: Int): Response<Image>

    // ==================== 播放历史 API ====================

    /**
     * 获取播放历史列表
     */
    @GET("history")
    suspend fun getHistory(
        @Query("page") page: Int = 1,
        @Query("page_size") pageSize: Int = 20
    ): Response<List<WatchHistory>>

    /**
     * 获取指定视频的观看历史
     */
    @GET("history/video/{videoId}")
    suspend fun getVideoHistory(@Path("videoId") videoId: Int): Response<WatchHistory?>

    /**
     * 更新/创建观看历史
     */
    @POST("history/video/{videoId}")
    suspend fun updateVideoHistory(
        @Path("videoId") videoId: Int,
        @Body body: Map<String, Any>
    ): Response<WatchHistory>

    /**
     * 删除观看历史
     */
    @DELETE("history/video/{videoId}")
    suspend fun deleteVideoHistory(@Path("videoId") videoId: Int): Response<Unit>

    /**
     * 清空所有历史
     */
    @POST("history/clear")
    suspend fun clearHistory(): Response<Unit>

    /**
     * 获取历史统计
     */
    @GET("history/stats")
    suspend fun getHistoryStats(): Response<HistoryStats>

    // ==================== 收藏 API ====================

    /**
     * 获取所有收藏
     */
    @GET("favorites")
    suspend fun getFavorites(): Response<FavoritesResponse>

    /**
     * 获取收藏统计
     */
    @GET("favorites/stats")
    suspend fun getFavoritesStats(): Response<Map<String, Int>>

    // ==================== 标签 API ====================

    /**
     * 获取所有标签
     */
    @GET("tags")
    suspend fun getTags(): Response<List<Tag>>

    /**
     * 创建标签
     */
    @POST("tags")
    suspend fun createTag(@Body body: Map<String, String>): Response<Tag>

    /**
     * 更新标签
     */
    @PUT("tags/{id}")
    suspend fun updateTag(
        @Path("id") tagId: Int,
        @Body body: Map<String, String>
    ): Response<Tag>

    /**
     * 删除标签
     */
    @DELETE("tags/{id}")
    suspend fun deleteTag(@Path("id") tagId: Int): Response<Unit>

    // ==================== 来源配置 API ====================

    /**
     * 获取来源列表
     */
    @GET("sources")
    suspend fun getSources(): Response<List<Source>>

    /**
     * 健康检查
     */
    @GET("health")
    suspend fun healthCheck(): Response<Map<String, Any>>
}
