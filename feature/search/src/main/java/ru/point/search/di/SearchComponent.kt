package ru.point.search.di

import dagger.Component
import ru.point.cars.di.CarsRepositoryModule
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule
import ru.point.search.ui.search.SearchFragment
import ru.point.search.ui.searchByFilters.SearchByFiltersFragment
import ru.point.search.ui.searchResults.SearchResultsFragment

@[FeatureScope Component(
    modules = [
        TokenStorageModule::class,
        CarsRepositoryModule::class
    ],
    dependencies = [FeatureDeps::class]
)]
internal interface SearchComponent {
    fun inject(searchByFiltersFragment: SearchByFiltersFragment)
    fun inject(searchResultsFragment: SearchResultsFragment)
    fun inject(searchFragment: SearchFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): SearchComponent
    }
}