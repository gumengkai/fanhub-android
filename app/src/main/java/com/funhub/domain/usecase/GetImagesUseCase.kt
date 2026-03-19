package com.funhub.domain.usecase

import com.funhub.domain.model.Image
import com.funhub.domain.model.Result
import com.funhub.domain.repository.ImageRepository
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(
        page: Int = 1,
        perPage: Int = 20,
        search: String? = null,
        tagId: Int? = null,
        favorite: Boolean? = null
    ): Result<List<Image>> {
        return repository.getImages(
            page = page,
            perPage = perPage,
            search = search,
            tagId = tagId,
            favorite = favorite
        )
    }
}
