package ru.point.cars.service

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.point.cars.model.AdDto
import ru.point.cars.model.BrandDto
import ru.point.cars.model.ModelDto
import ru.point.common.model.ResponseMessage

interface CarsService {
    @GET("/brands")
    suspend fun getBrands(): Result<List<BrandDto>>

    @GET("/models/{brandName}")
    suspend fun getModelsByBrand(@Path("brandName") brandName: String): Result<List<ModelDto>>

    @GET("/cars")
    suspend fun getCars(@Query("query") query: String = ""): Result<List<AdDto>>

    @GET("/cars/{adId}")
    suspend fun getCarAdById(@Path("adId") adId: String): Result<AdDto>

    @GET("/profile/{userId}/ads")
    suspend fun getUsersAds(@Path("userId") userId: String): Result<List<AdDto>>

    @DELETE("/profile/{userId}/ads/{adId}")
    suspend fun deleteAd(
        @Path("userId") userId: String,
        @Path("adId") adId: String
    ): Result<ResponseMessage>

    @GET("/profile/{userId}/favourites")
    suspend fun getUsersFavourite(@Path("userId") userId: String): Result<List<AdDto>>

    @POST("/profile/{userId}/favourites/{adId}")
    suspend fun addCarToFavourites(
        @Path("userId") userId: String,
        @Path("adId") adId: String
    ): Result<ResponseMessage>

    @DELETE("/profile/{userId}/favourites/{adId}")
    suspend fun removeCarFromFavourites(
        @Path("userId") userId: String,
        @Path("adId") adId: String
    ): Result<ResponseMessage>

    @GET("/cars/filters")
    suspend fun searchCarsByFilters(
        @Query("brand") brand: String? = null,
        @Query("model") model: String? = null,
        @Query("yearMin") yearMin: Short? = null,
        @Query("yearMax") yearMax: Short? = null,
        @Query("priceMin") priceMin: Int? = null,
        @Query("priceMax") priceMax: Int? = null,
        @Query("mileageMin") mileageMin: Int? = null,
        @Query("mileageMax") mileageMax: Int? = null,
        @Query("enginePowerMin") enginePowerMin: Short? = null,
        @Query("enginePowerMax") enginePowerMax: Short? = null,
        @Query("engineCapacityMin") engineCapacityMin: Double? = null,
        @Query("engineCapacityMax") engineCapacityMax: Double? = null,
        @Query("fuelType") fuelType: String? = null,
        @Query("bodyType") bodyType: String? = null,
        @Query("color") color: String? = null,
        @Query("transmission") transmission: String? = null,
        @Query("drivetrain") drivetrain: String? = null,
        @Query("wheel") wheel: String? = null,
        @Query("condition") condition: String? = null,
        @Query("owners") owners: String? = null
    ): Result<List<AdDto>>
}