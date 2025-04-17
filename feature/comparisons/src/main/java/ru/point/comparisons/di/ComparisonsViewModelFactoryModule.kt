package ru.point.comparisons.di

import dagger.Module
import dagger.Provides
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.comparisons.ui.ComparisonsViewModelFactory

@Module
internal class ComparisonsViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideComparisonsViewModelFactory(carsRepository: CarsRepository) =
        ComparisonsViewModelFactory(carsRepository = carsRepository)
}