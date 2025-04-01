package ru.point.car_details.di

import dagger.Module
import dagger.Provides
import ru.point.car_details.ui.CarDetailsViewModelFactory
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.user.repository.UserRepository

@Module
internal class CarDetailsViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideCarDetailsViewModelFactory(
        carsRepository: CarsRepository,
        userRepository: UserRepository
    ) =
        CarDetailsViewModelFactory(
            carsRepository = carsRepository,
            userRepository = userRepository
        )
}