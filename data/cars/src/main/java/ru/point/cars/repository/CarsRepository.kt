package ru.point.cars.repository

import ru.point.cars.model.BrandDto
import ru.point.cars.model.ModelDto

interface CarsRepository {
    suspend fun getBrands(): Result<List<BrandDto>>
    suspend fun getModelsByBrand(brandName: String): Result<List<ModelDto>>
}