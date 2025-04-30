package ru.point.cars.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
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
    suspend fun getCars(
        @Query("query") query: String,
        @Query("sortParam") sortParam: String,
        @Query("orderParam") orderParam: String
    ): Result<List<AdDto>>

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
        @QueryMap filterParams: Map<String, String>,
        @Query("sortParam") sortParam: String,
        @Query("orderParam") orderParam: String
    ): Result<List<AdDto>>

    @Multipart
    @POST("/profile/{userId}/ads")
    suspend fun createNewAd(
        @Path("userId") userId: String,
        @Part("car") car: RequestBody,
        @Part photos: List<MultipartBody.Part>
    ): Result<ResponseMessage>

    @Multipart
    @PATCH("/profile/{userId}/ads/{adId}")
    suspend fun updateAd(
        @Path("userId") userId: String,
        @Path("adId") adId: String,
        @Part("car") car: RequestBody,
        @Part newPhotos: List<MultipartBody.Part>? = null,
        @Part("removePhotoIds") removePhotoIds: RequestBody? = null
    ): Result<ResponseMessage>

    @GET("/profile/{userId}/comparisons")
    suspend fun getUsersComparisons(@Path("userId") userId: String): Result<List<AdDto>>

    @POST("/profile/{userId}/comparisons/{adId}")
    suspend fun addCarToComparisons(
        @Path("userId") userId: String,
        @Path("adId") adId: String
    ): Result<ResponseMessage>

    @DELETE("/profile/{userId}/comparisons/{adId}")
    suspend fun removeCarFromComparisons(
        @Path("userId") userId: String,
        @Path("adId") adId: String
    ): Result<ResponseMessage>
}