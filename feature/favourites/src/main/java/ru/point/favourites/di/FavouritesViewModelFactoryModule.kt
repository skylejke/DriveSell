package ru.point.favourites.di

import dagger.Module
import dagger.Provides
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.favourites.ui.FavouritesViewModelFactory

@Module
internal class FavouritesViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideFavouritesViewModelFactory(carsRepository: CarsRepository) =
        FavouritesViewModelFactory(carsRepository = carsRepository)
}