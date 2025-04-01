package ru.point.search.di

import dagger.Module
import dagger.Provides
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.search.ui.SearchViewModelFactory

@Module
internal class SearchViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideSearchViewModelFactory(carsRepository: CarsRepository) =
        SearchViewModelFactory(carsRepository = carsRepository)
}