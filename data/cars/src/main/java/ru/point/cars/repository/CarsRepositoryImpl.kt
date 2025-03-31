package ru.point.cars.repository

import com.auth0.jwt.JWT
import ru.point.cars.service.CarsService
import ru.point.common.storage.TokenStorage

class CarsRepositoryImpl(
    private val carsService: CarsService,
    private val tokenStorage: TokenStorage
) : CarsRepository {
    override suspend fun getBrands() =
        carsService.getBrands()

    override suspend fun getModelsByBrand(brandName: String) =
        carsService.getModelsByBrand(brandName)

    override suspend fun getCars(query: String) =
        carsService.getCars(query)

    override suspend fun getCarAdById(adId: String) =
        carsService.getCarAdById(adId)

    override suspend fun getUsersAds() =
        carsService.getUsersAds(userId = JWT.decode(tokenStorage.token).subject)
}