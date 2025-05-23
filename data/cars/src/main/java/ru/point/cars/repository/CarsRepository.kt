package ru.point.cars.repository

import android.net.Uri
import kotlinx.coroutines.flow.Flow
import ru.point.cars.model.AdDto
import ru.point.cars.model.BrandDto
import ru.point.cars.model.CreateCarRequest
import ru.point.cars.model.EditCarRequest
import ru.point.cars.model.ModelDto
import ru.point.cars.model.SearchHistory
import ru.point.common.model.CarFilterParams
import ru.point.common.model.ResponseMessage

interface CarsRepository {

    suspend fun getBrands(): Result<List<BrandDto>>

    suspend fun getModelsByBrand(brandName: String): Result<List<ModelDto>>

    suspend fun getCars(query: String, sortParam: String, orderParam: String): Result<List<AdDto>>

    suspend fun getCarAdById(adId: String): Result<AdDto>

    suspend fun getUsersAds(): Result<List<AdDto>>

    suspend fun deleteAd(adId: String): Result<ResponseMessage>

    suspend fun getUsersFavourites(): Result<List<AdDto>>

    suspend fun addCarToFavourites(adId: String): Result<ResponseMessage>

    suspend fun removeCarFromFavourites(adId: String): Result<ResponseMessage>

    suspend fun checkIsFavourite(adId: String): Result<Boolean>

    suspend fun searchCarsByFilters(
        filterParams: CarFilterParams,
        sortParam: String,
        orderParam: String
    ): Result<List<AdDto>>

    suspend fun createNewAd(car: CreateCarRequest, photos: List<Uri>): Result<ResponseMessage>

    suspend fun updateAd(
        adId: String,
        car: EditCarRequest,
        newPhotos: List<Uri>,
        removePhotoIds: List<String>
    ): Result<ResponseMessage>

    suspend fun getUsersComparisons(): Result<List<AdDto>>

    suspend fun addCarToComparisons(adId: String): Result<ResponseMessage>

    suspend fun removeCarFromComparisons(adId: String): Result<ResponseMessage>

    suspend fun checkIsInComparisons(adId: String): Result<Boolean>

    suspend fun insertSearchHistoryItem(query: String)

    suspend fun getSearchHistory(): Flow<List<SearchHistory>>

    suspend fun clearSearchHistory()
}