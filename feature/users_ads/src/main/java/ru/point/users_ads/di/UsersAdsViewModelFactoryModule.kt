package ru.point.users_ads.di

import dagger.Module
import dagger.Provides
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.users_ads.ui.UsersAdsViewModelFactory

@Module
internal class UsersAdsViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideUsersAdsViewModelFactory(carsRepository: CarsRepository) = UsersAdsViewModelFactory(carsRepository = carsRepository)
}