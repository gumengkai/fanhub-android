package com.funhub.domain.usecase

import com.funhub.domain.model.Result
import com.funhub.domain.model.VideoClipInfo
import com.funhub.domain.repository.VideoRepository
import javax.inject.Inject

class GetClipInfoUseCase @Inject constructor(
    private val repository: VideoRepository
) {
    suspend operator fun invoke(videoId: String): Result<VideoClipInfo> {
        return repository.getClipInfo(videoId)
    }
}
