package ru.point.search.di

import dagger.Module
import dagger.Provides
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.search.ui.search.SearchViewModelFactory
import ru.point.search.ui.searchByFilters.SearchByFiltersViewModelFactory
import ru.point.search.ui.searchResults.SearchResultsViewModelFactory

@Module
internal class SearchViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideSearchByFiltersViewModelFactory(carsRepository: CarsRepository) =
        SearchByFiltersViewModelFactory(carsRepository = carsRepository)

    @[Provides FeatureScope]
    fun provideSearchResultsViewModelFactory(carsRepository: CarsRepository) =
        SearchResultsViewModelFactory(carsRepository = carsRepository)

    @[Provides FeatureScope]
    fun provideSearchViewModelFactory(carsRepository: CarsRepository) =
        SearchViewModelFactory(carsRepository = carsRepository)
}