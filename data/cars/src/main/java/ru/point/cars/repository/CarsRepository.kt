package ru.point.cars.repository

import ru.point.cars.model.AdDto
import ru.point.cars.model.BrandDto
import ru.point.cars.model.ModelDto
import ru.point.common.model.ResponseMessage

interface CarsRepository {
    suspend fun getBrands(): Result<List<BrandDto>>
    suspend fun getModelsByBrand(brandName: String): Result<List<ModelDto>>
    suspend fun getCars(query: String = ""): Result<List<AdDto>>
    suspend fun getCarAdById(adId: String): Result<AdDto>
    suspend fun getUsersAds(): Result<List<AdDto>>
    suspend fun deleteAd(adId: String): Result<ResponseMessage>
    suspend fun getUsersFavourites(): Result<List<AdDto>>
    suspend fun addCarToFavourites(adId: String): Result<ResponseMessage>
    suspend fun removeCarFromFavourites(adId: String): Result<ResponseMessage>
    suspend fun checkIsFavourite(adId: String): Result<Boolean>
}