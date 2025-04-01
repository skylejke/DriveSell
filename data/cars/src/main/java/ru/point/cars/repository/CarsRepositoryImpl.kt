package ru.point.cars.repository

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
        carsService.getUsersAds(userId = tokenStorage.getUserId())

    override suspend fun deleteAd(adId: String) =
        carsService.deleteAd(tokenStorage.getUserId(), adId)

    override suspend fun getUsersFavourites() =
        carsService.getUsersFavourite(userId = tokenStorage.getUserId())

    override suspend fun addCarToFavourites(adId: String) =
        carsService.addCarToFavourites(
            userId = tokenStorage.getUserId(),
            adId = adId
        )

    override suspend fun removeCarFromFavourites(adId: String) =
        carsService.removeCarFromFavourites(
            userId = tokenStorage.getUserId(),
            adId = adId
        )

    override suspend fun checkIsFavourite(adId: String) =
        getUsersFavourites().map {
            it.contains(getCarAdById(adId).getOrNull())
        }
}