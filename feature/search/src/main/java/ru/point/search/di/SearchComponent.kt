package ru.point.search.di

import dagger.Component
import ru.point.cars.di.CarsRepositoryModule
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule
import ru.point.search.ui.SearchFragment

@[FeatureScope Component(
    modules = [TokenStorageModule::class, CarsRepositoryModule::class, SearchUseCaseModule::class, SearchViewModelFactoryModule::class],
    dependencies = [FeatureDeps::class]
)]
internal interface SearchComponent {
    fun inject(searchFragment: SearchFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): SearchComponent
    }
}