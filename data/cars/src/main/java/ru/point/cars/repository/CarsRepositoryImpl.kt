package ru.point.cars.repository

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody
import ru.point.cars.model.AdDto
import ru.point.cars.model.CreateCarRequest
import ru.point.cars.model.EditCarRequest
import ru.point.cars.model.SearchHistory
import ru.point.cars.model.asRequestBody
import ru.point.cars.room.DataBase
import ru.point.cars.service.CarsService
import ru.point.common.ext.uriToMultipart
import ru.point.common.model.ResponseMessage
import ru.point.common.storage.TokenStorage

class CarsRepositoryImpl(
    private val carsService: CarsService,
    private val tokenStorage: TokenStorage,
    private val dataBase: DataBase,
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CarsRepository {

    override suspend fun getBrands() = withContext(dispatcher) {
        carsService.getBrands()
    }

    override suspend fun getModelsByBrand(brandName: String) = withContext(dispatcher) {
        carsService.getModelsByBrand(brandName = brandName)
    }

    override suspend fun getCars(query: String, sortParam: String, orderParam: String) = withContext(dispatcher) {
        carsService.getCars(query = query, sortParam = sortParam, orderParam = orderParam)
    }

    override suspend fun getCarAdById(adId: String) = withContext(dispatcher) {
        carsService.getCarAdById(adId = adId)
    }

    override suspend fun getUsersAds() = withContext(dispatcher) {
        carsService.getUsersAds(userId = tokenStorage.getUserId())
    }

    override suspend fun deleteAd(adId: String) = withContext(dispatcher) {
        carsService.deleteAd(userId = tokenStorage.getUserId(), adId = adId)
    }

    override suspend fun getUsersFavourites() = withContext(dispatcher) {
        carsService.getUsersFavourite(userId = tokenStorage.getUserId())
    }

    override suspend fun addCarToFavourites(adId: String) = withContext(dispatcher) {
        carsService.addCarToFavourites(userId = tokenStorage.getUserId(), adId = adId)
    }

    override suspend fun removeCarFromFavourites(adId: String) = withContext(dispatcher) {
        carsService.removeCarFromFavourites(userId = tokenStorage.getUserId(), adId = adId)
    }

    override suspend fun checkIsFavourite(adId: String) = withContext(dispatcher) {
        getUsersFavourites().map {
            it.contains(getCarAdById(adId).getOrNull())
        }
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
        owners: String?,
        sortParam: String,
        orderParam: String
    ) = withContext(dispatcher) {
        carsService.searchCarsByFilters(
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
            sortParam = sortParam,
            orderParam = orderParam
        )
    }

    override suspend fun createNewAd(car: CreateCarRequest, photos: List<Uri>) = withContext(dispatcher) {
        carsService.createNewAd(
            userId = tokenStorage.getUserId(),
            car = car.asRequestBody,
            photos = photos.map {
                it.uriToMultipart(context)
            }
        )
    }

    override suspend fun updateAd(
        adId: String,
        car: EditCarRequest,
        newPhotos: List<Uri>,
        removePhotoIds: List<String>
    ) = withContext(dispatcher) {
        carsService.updateAd(
            userId = tokenStorage.getUserId(),
            adId = adId,
            car = car.asRequestBody,
            newPhotos = if (newPhotos.isNotEmpty()) newPhotos.map { it.uriToMultipart(context) } else null,
            removePhotoIds = if (removePhotoIds.isNotEmpty())
                removePhotoIds.joinToString(separator = ",").toRequestBody() else null
        )
    }

    override suspend fun getUsersComparisons(): Result<List<AdDto>> = withContext(dispatcher) {
        carsService.getUsersComparisons(userId = tokenStorage.getUserId())
    }

    override suspend fun addCarToComparisons(adId: String): Result<ResponseMessage> = withContext(dispatcher) {
        carsService.addCarToComparisons(userId = tokenStorage.getUserId(), adId = adId)
    }

    override suspend fun removeCarFromComparisons(adId: String): Result<ResponseMessage> = withContext(dispatcher) {
        carsService.removeCarFromComparisons(userId = tokenStorage.getUserId(), adId = adId)
    }

    override suspend fun checkIsInComparisons(adId: String) = withContext(dispatcher) {
        getUsersComparisons().map {
            it.contains(getCarAdById(adId).getOrNull())
        }
    }

    override suspend fun insertSearchHistoryItem(query: String) = withContext(dispatcher) {
        dataBase.getSearchHistoryDao().insertSearchHistoryItem(
            SearchHistory(id = null, query = query, userId = tokenStorage.getUserId())
        )
    }

    override suspend fun getSearchHistory() = withContext(dispatcher) {
        dataBase.getSearchHistoryDao().getSearchHistory(tokenStorage.getUserId())
    }

    override suspend fun clearSearchHistory() = withContext(dispatcher) {
        dataBase.getSearchHistoryDao().clearSearchHistory()
    }
}