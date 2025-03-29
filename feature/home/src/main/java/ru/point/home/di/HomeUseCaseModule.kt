package ru.point.home.di

import dagger.Module
import dagger.Provides
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.home.domain.GetCarsUseCase

@Module
internal class HomeUseCaseModule {

    @[Provides FeatureScope]
    fun provideGetCarsUseCase(carsRepository: CarsRepository) =
        GetCarsUseCase(carsRepository = carsRepository)
}