package com.funhub.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("url") val url: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?,
    @SerializedName("width") val width: Int,
    @SerializedName("height") val height: Int,
    @SerializedName("file_size") val fileSize: Long,
    @SerializedName("created_at") val createdAt: Long
)

data class ImageListResponse(
    @SerializedName("images") val images: List<ImageDto>,
    @SerializedName("total") val total: Int
)
