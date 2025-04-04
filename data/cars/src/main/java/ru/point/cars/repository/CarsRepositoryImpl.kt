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

    override suspend fun searchCarsByFilters(
        brand: String?,
        model: String?,
        yearMin: Short?,
        yearMax: Short?,
        priceMin: Int?,
        priceMax: Int?,
        mileageMin: Int?,
        mileageMax: Int?,
        enginePowerMin: Short?,
        enginePowerMax: Short?,
        engineCapacityMin: Double?,
        engineCapacityMax: Double?,
        fuelType: String?,
        bodyType: String?,
        color: String?,
        transmission: String?,
        drivetrain: String?,
        wheel: String?,
        condition: String?,
        owners: String?
    ) = carsService.searchCarsByFilters(
        brand = brand,
        model = model,
        yearMin = yearMin,
        yearMax = yearMax,
        priceMin = priceMin,
        priceMax = priceMax,
        mileageMin = mileageMin,
        mileageMax = mileageMax,
        enginePowerMin = enginePowerMin,
        enginePowerMax = enginePowerMax,
        engineCapacityMin = engineCapacityMin,
        engineCapacityMax = engineCapacityMax,
        fuelType = fuelType,
        bodyType = bodyType,
        color = color,
        transmission = transmission,
        drivetrain = drivetrain,
        wheel = wheel,
        condition = condition,
        owners = owners,
    )
}