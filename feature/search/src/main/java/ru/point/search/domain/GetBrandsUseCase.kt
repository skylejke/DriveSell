package ru.point.search.domain

import ru.point.cars.repository.CarsRepository
import ru.point.search.domain.model.asBrandVo

internal class GetBrandsUseCase(private val carsRepository: CarsRepository) {
    suspend operator fun invoke() = carsRepository.getBrands().map { brandList ->
        brandList.map { it.asBrandVo }
    }
}