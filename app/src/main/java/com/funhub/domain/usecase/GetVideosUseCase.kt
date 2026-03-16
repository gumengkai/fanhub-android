package com.funhub.domain.usecase

import com.funhub.domain.model.Result
import com.funhub.domain.model.Video
import com.funhub.domain.repository.VideoRepository
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val repository: VideoRepository
) {
    suspend operator fun invoke(): Result<List<Video>> {
        return repository.getVideos()
    }
}
