package ru.point.search.domain

import ru.point.cars.repository.CarsRepository
import ru.point.search.domain.model.asModelVo

internal class GetModelsByBrandUseCase(private val carsRepository: CarsRepository) {
    suspend operator fun invoke(brandName: String) =
        carsRepository.getModelsByBrand(brandName).map { modelList ->
            modelList.map { it.asModelVo }
        }
}