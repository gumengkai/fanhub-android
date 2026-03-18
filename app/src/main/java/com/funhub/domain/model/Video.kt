package com.funhub.domain.model

data class Video(
    val id: String,
    val title: String,
    val description: String? = "",
    val thumbnailUrl: String? = null,
    val streamUrl: String,
    val videoUrl: String = streamUrl, // Alias for TikTok style player
    val duration: Long = 0,
    val fileSize: Long = 0,
    val createdAt: Long = 0,
    val isFavorite: Boolean = false,
    val likeCount: Int = 0,
    val viewCount: Int = 0,
    val creatorName: String = "",
    val creatorAvatar: String? = null
)

data class VideoClipInfo(
    val id: String,
    val title: String,
    val duration: Long,
    val thumbnailUrl: String?
)
