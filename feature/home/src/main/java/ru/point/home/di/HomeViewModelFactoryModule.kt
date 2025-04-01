package ru.point.home.di

import dagger.Module
import dagger.Provides
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.home.ui.HomeViewModelFactory

@Module
internal class HomeViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideHomeViewModelFactory(carsRepository: CarsRepository) =
        HomeViewModelFactory(carsRepository = carsRepository)
}