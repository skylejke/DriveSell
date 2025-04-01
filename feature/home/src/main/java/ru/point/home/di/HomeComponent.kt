package ru.point.home.di

import dagger.Component
import ru.point.cars.di.CarsRepositoryModule
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule
import ru.point.home.ui.HomeFragment

@[FeatureScope Component(
    modules = [
        TokenStorageModule::class,
        CarsRepositoryModule::class,
        HomeViewModelFactoryModule::class
    ],
    dependencies = [FeatureDeps::class]
)]
internal interface HomeComponent {
    fun inject(homeFragment: HomeFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): HomeComponent
    }
}