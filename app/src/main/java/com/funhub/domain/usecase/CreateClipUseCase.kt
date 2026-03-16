package com.funhub.domain.usecase

import com.funhub.domain.model.Result
import com.funhub.domain.repository.VideoRepository
import javax.inject.Inject

class CreateClipUseCase @Inject constructor(
    private val repository: VideoRepository
) {
    suspend operator fun invoke(videoId: String, startTime: Long, endTime: Long): Result<String> {
        return repository.createClip(videoId, startTime, endTime)
    }
}
