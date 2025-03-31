package ru.point.cars.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.point.cars.repository.CarsRepository
import ru.point.cars.repository.CarsRepositoryImpl
import ru.point.cars.service.CarsService
import ru.point.common.di.FeatureScope
import ru.point.common.storage.TokenStorage

@Module
class CarsRepositoryModule {

    @[Provides FeatureScope]
    fun provideCarsRepository(
        carsService: CarsService,
        tokenStorage: TokenStorage
    ): CarsRepository =
        CarsRepositoryImpl(carsService = carsService, tokenStorage = tokenStorage)

    @[Provides FeatureScope]
    fun provideCarsService(retrofit: Retrofit) = retrofit.create<CarsService>()
}