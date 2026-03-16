package com.funhub.domain.model

data class Image(
    val id: String,
    val title: String,
    val url: String,
    val thumbnailUrl: String?,
    val width: Int,
    val height: Int,
    val fileSize: Long,
    val createdAt: Long,
    val isFavorite: Boolean = false
)
