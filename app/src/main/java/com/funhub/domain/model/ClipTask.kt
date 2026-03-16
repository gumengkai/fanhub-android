package com.funhub.domain.model

data class ClipTask(
    val id: String,
    val videoId: String,
    val videoTitle: String,
    val startTime: Long,
    val endTime: Long,
    val status: ClipStatus,
    val outputPath: String? = null,
    val errorMessage: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

enum class ClipStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED
}
