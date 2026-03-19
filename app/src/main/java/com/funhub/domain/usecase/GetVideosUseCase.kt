package com.funhub.domain.usecase

import com.funhub.domain.model.Result
import com.funhub.domain.model.Video
import com.funhub.domain.repository.VideoRepository
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(
    private val repository: VideoRepository
) {
    suspend operator fun invoke(
        page: Int = 1,
        perPage: Int = 20,
        search: String? = null,
        tagId: Int? = null,
        favorite: Boolean? = null,
        sortBy: String = "created_at",
        order: String = "desc"
    ): Result<List<Video>> {
        return repository.getVideos(
            page = page,
            perPage = perPage,
            search = search,
            tagId = tagId,
            favorite = favorite,
            sortBy = sortBy,
            order = order
        )
    }
}
