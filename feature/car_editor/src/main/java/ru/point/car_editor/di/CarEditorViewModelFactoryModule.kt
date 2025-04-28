package ru.point.car_editor.di

import dagger.Module
import dagger.Provides
import ru.point.car_editor.ui.create.CreateCarViewModelFactory
import ru.point.car_editor.ui.edit.EditCarViewModelFactory
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.common.utils.ResourceProvider
import ru.point.user.repository.UserRepository
import javax.inject.Named

@Module
internal class CarEditorViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideCreateCarViewModelFactory(
        carsRepository: CarsRepository,
        userRepository: UserRepository,
        resourceProvider: ResourceProvider
    ) =
        CreateCarViewModelFactory(
            carsRepository = carsRepository,
            userRepository = userRepository,
            resourceProvider = resourceProvider
        )

    @[Provides FeatureScope]
    fun provideEditCarViewModelFactory(carsRepository: CarsRepository, @Named("adId") adId: String) =
        EditCarViewModelFactory(carsRepository = carsRepository, adId = adId)
}