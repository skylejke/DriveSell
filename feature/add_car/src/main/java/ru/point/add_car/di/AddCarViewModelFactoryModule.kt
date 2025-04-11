package ru.point.add_car.di

import dagger.Module
import dagger.Provides
import ru.point.add_car.ui.AddCarViewModelFactory
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.user.repository.UserRepository

@Module
internal class AddCarViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideAddCarViewModelFactory(carsRepository: CarsRepository, userRepository: UserRepository) =
        AddCarViewModelFactory(carsRepository = carsRepository, userRepository = userRepository)
}