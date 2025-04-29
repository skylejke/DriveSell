package ru.point.favourites.di

import dagger.Component
import ru.point.cars.di.CarsRepositoryModule
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule
import ru.point.favourites.ui.FavouritesFragment

@[FeatureScope Component(
    modules = [TokenStorageModule::class, CarsRepositoryModule::class],
    dependencies = [FeatureDeps::class]
)]
internal interface FavouritesComponent {
    fun inject(favouritesFragment: FavouritesFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): FavouritesComponent
    }
}