package ru.point.cars.service

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.point.cars.model.AdDto
import ru.point.cars.model.BrandDto
import ru.point.cars.model.ModelDto

interface CarsService {
    @GET("/brands")
    suspend fun getBrands(): Result<List<BrandDto>>

    @GET("/models/{brandName}")
    suspend fun getModelsByBrand(@Path("brandName") brandName: String): Result<List<ModelDto>>

    @GET("/cars")
    suspend fun getCars(@Query("query") query: String = ""): Result<List<AdDto>>
}