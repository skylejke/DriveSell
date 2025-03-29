package ru.point.search.di

import dagger.Module
import dagger.Provides
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.search.domain.GetBrandsUseCase
import ru.point.search.domain.GetModelsByBrandUseCase

@Module
internal class SearchUseCaseModule {

    @[Provides FeatureScope]
    fun provideGetBrandsUseCase(carsRepository: CarsRepository) =
        GetBrandsUseCase(carsRepository = carsRepository)

    @[Provides FeatureScope]
    fun provideGetModelsByBrandUseCase(carsRepository: CarsRepository) =
        GetModelsByBrandUseCase(carsRepository = carsRepository)
}