package ru.point.cars.repository

import ru.point.cars.service.CarsService

class CarsRepositoryImpl(private val carsService: CarsService) : CarsRepository {
    override suspend fun getBrands() =
        carsService.getBrands()

    override suspend fun getModelsByBrand(brandName: String) =
        carsService.getModelsByBrand(brandName)

    override suspend fun getCars(query: String) =
        carsService.getCars(query)

    override suspend fun getCarAdById(adId: String) =
        carsService.getCarAdById(adId)
}