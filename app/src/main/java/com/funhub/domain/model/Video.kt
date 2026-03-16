package com.funhub.domain.model

data class Video(
    val id: String,
    val title: String,
    val description: String?,
    val thumbnailUrl: String?,
    val streamUrl: String,
    val duration: Long,
    val fileSize: Long,
    val createdAt: Long,
    val isFavorite: Boolean = false
)

data class VideoClipInfo(
    val id: String,
    val title: String,
    val duration: Long,
    val thumbnailUrl: String?
)
