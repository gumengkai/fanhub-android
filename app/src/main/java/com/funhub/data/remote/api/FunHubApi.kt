package com.funhub.data.remote.api

import com.funhub.data.remote.dto.ClipRequest
import com.funhub.data.remote.dto.ClipResponse
import com.funhub.data.remote.dto.CreateTagRequest
import com.funhub.data.remote.dto.FFmpegCheckResponse
import com.funhub.data.remote.dto.HealthResponse
import com.funhub.data.remote.dto.ImageListResponse
import com.funhub.data.remote.dto.TagDto
import com.funhub.data.remote.dto.UpdateImageRequest
import com.funhub.data.remote.dto.UpdateTagRequest
import com.funhub.data.remote.dto.UpdateVideoRequest
import com.funhub.data.remote.dto.VideoClipInfoDto
import com.funhub.data.remote.dto.VideoListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import okhttp3.ResponseBody

interface FunHubApi {
    
    // Health
    @GET("/api/health")
    suspend fun checkHealth(): Response<HealthResponse>
    
    // Videos
    @GET("/api/videos")
    suspend fun getVideos(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("search") search: String? = null,
        @Query("tag_id") tagId: Int? = null,
        @Query("favorite") favorite: Boolean? = null,
        @Query("sort_by") sortBy: String = "created_at",
        @Query("order") order: String = "desc"
    ): Response<VideoListResponse>
    
    @GET("/api/videos/{id}")
    suspend fun getVideo(@Path("id") id: String): Response<VideoDto>
    
    @PUT("/api/videos/{id}")
    suspend fun updateVideo(
        @Path("id") id: String,
        @Body request: UpdateVideoRequest
    ): Response<VideoDto>
    
    @DELETE("/api/videos/{id}")
    suspend fun deleteVideo(@Path("id") id: String): Response<Unit>
    
    @GET("/api/videos/{id}/stream")
    @Streaming
    suspend fun streamVideo(@Path("id") id: String): Response<ResponseBody>
    
    // Images
    @GET("/api/images")
    suspend fun getImages(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 20,
        @Query("search") search: String? = null,
        @Query("tag_id") tagId: Int? = null,
        @Query("favorite") favorite: Boolean? = null
    ): Response<ImageListResponse>
    
    @GET("/api/images/{id}")
    suspend fun getImage(@Path("id") id: String): Response<ImageDto>
    
    @PUT("/api/images/{id}")
    suspend fun updateImage(
        @Path("id") id: String,
        @Body request: UpdateImageRequest
    ): Response<ImageDto>
    
    @DELETE("/api/images/{id}")
    suspend fun deleteImage(@Path("id") id: String): Response<Unit>
    
    // Tags
    @GET("/api/tags")
    suspend fun getTags(): Response<List<TagDto>>
    
    @POST("/api/tags")
    suspend fun createTag(@Body request: CreateTagRequest): Response<TagDto>
    
    @PUT("/api/tags/{id}")
    suspend fun updateTag(
        @Path("id") id: Int,
        @Body request: UpdateTagRequest
    ): Response<TagDto>
    
    @DELETE("/api/tags/{id}")
    suspend fun deleteTag(@Path("id") id: Int): Response<Unit>
    
    // Clips
    @GET("/api/clips/{id}/info")
    suspend fun getClipInfo(@Path("id") id: String): Response<VideoClipInfoDto>
    
    @POST("/api/clips/{id}/clip")
    suspend fun createClip(
        @Path("id") id: String,
        @Body request: ClipRequest
    ): Response<ClipResponse>
    
    @GET("/api/clips/check")
    suspend fun checkFFmpeg(): Response<FFmpegCheckResponse>
}

data class VideoDto(
    val id: String,
    val title: String,
    val description: String?,
    val thumbnailUrl: String?,
    val streamUrl: String,
    val duration: Long,
    val fileSize: Long,
    val createdAt: Long,
    val isFavorite: Boolean,
    val viewCount: Int,
    val tags: List<TagDto>?
)

data class ImageDto(
    val id: String,
    val title: String,
    val url: String,
    val thumbnailUrl: String?,
    val width: Int,
    val height: Int,
    val fileSize: Long,
    val createdAt: Long,
    val isFavorite: Boolean,
    val tags: List<TagDto>?
)
