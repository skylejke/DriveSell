package ru.point.cars.repository

import ru.point.cars.model.AdDto
import ru.point.cars.model.BrandDto
import ru.point.cars.model.ModelDto

interface CarsRepository {
    suspend fun getBrands(): Result<List<BrandDto>>
    suspend fun getModelsByBrand(brandName: String): Result<List<ModelDto>>
    suspend fun getCars(query: String = ""): Result<List<AdDto>>
    suspend fun getCarAdById(adId: String): Result<AdDto>
}