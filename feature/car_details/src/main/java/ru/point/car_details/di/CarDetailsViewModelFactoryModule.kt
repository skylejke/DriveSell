package ru.point.car_details.di

import dagger.Module
import dagger.Provides
import ru.point.car_details.ui.CarDetailsViewModelFactory
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.user.repository.UserRepository
import javax.inject.Named

@Module
internal class CarDetailsViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideCarDetailsViewModelFactory(
        carsRepository: CarsRepository,
        userRepository: UserRepository,
        @Named("adId") adId: String,
        @Named("userId") userId: String
    ) =
        CarDetailsViewModelFactory(
            carsRepository = carsRepository,
            userRepository = userRepository,
            adId = adId,
            userId = userId
        )
}