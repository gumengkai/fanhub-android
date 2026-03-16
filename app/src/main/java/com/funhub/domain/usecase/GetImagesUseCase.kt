package com.funhub.domain.usecase

import com.funhub.domain.model.Image
import com.funhub.domain.model.Result
import com.funhub.domain.repository.ImageRepository
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(
    private val repository: ImageRepository
) {
    suspend operator fun invoke(): Result<List<Image>> {
        return repository.getImages()
    }
}
