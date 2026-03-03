package com.fanhub.android.data.model

import com.google.gson.annotations.SerializedName

data class FavoriteStats(
    @SerializedName("total_videos") val totalVideos: Int,
    @SerializedName("total_images") val totalImages: Int,
    @SerializedName("total") val total: Int
)

data class FavoriteItem(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String, // "video" or "image"
    @SerializedName("title") val title: String,
    @SerializedName("thumbnail_url") val thumbnailUrl: String?,
    @SerializedName("created_at") val createdAt: String
)

data class FavoriteListResponse(
    @SerializedName("items") val items: List<FavoriteItem>,
    @SerializedName("total") val total: Int,
    @SerializedName("pages") val pages: Int,
    @SerializedName("current_page") val currentPage: Int,
    @SerializedName("per_page") val perPage: Int
)
