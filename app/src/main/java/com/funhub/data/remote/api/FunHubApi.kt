package com.funhub.data.remote.api

import com.funhub.data.remote.dto.ClipRequest
import com.funhub.data.remote.dto.ClipResponse
import com.funhub.data.remote.dto.FFmpegCheckResponse
import com.funhub.data.remote.dto.HealthResponse
import com.funhub.data.remote.dto.ImageListResponse
import com.funhub.data.remote.dto.VideoClipInfoDto
import com.funhub.data.remote.dto.VideoListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming
import okhttp3.ResponseBody

interface FunHubApi {
    
    @GET("/api/health")
    suspend fun checkHealth(): Response<HealthResponse>
    
    @GET("/api/videos")
    suspend fun getVideos(): Response<VideoListResponse>
    
    @GET("/api/videos/{id}/stream")
    @Streaming
    suspend fun streamVideo(@Path("id") id: String): Response<ResponseBody>
    
    @GET("/api/clips/{id}/info")
    suspend fun getClipInfo(@Path("id") id: String): Response<VideoClipInfoDto>
    
    @POST("/api/clips/{id}/clip")
    suspend fun createClip(
        @Path("id") id: String,
        @Body request: ClipRequest
    ): Response<ClipResponse>
    
    @GET("/api/clips/check")
    suspend fun checkFFmpeg(): Response<FFmpegCheckResponse>
    
    @GET("/api/images")
    suspend fun getImages(): Response<ImageListResponse>
}
