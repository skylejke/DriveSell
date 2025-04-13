package ru.point.car_editor.di

import dagger.Module
import dagger.Provides
import ru.point.car_editor.ui.create.CreateCarViewModelFactory
import ru.point.car_editor.ui.edit.EditCarViewModelFactory
import ru.point.cars.repository.CarsRepository
import ru.point.common.di.FeatureScope
import ru.point.user.repository.UserRepository

@Module
internal class CarEditorViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideCreateCarViewModelFactory(carsRepository: CarsRepository, userRepository: UserRepository) =
        CreateCarViewModelFactory(carsRepository = carsRepository, userRepository = userRepository)

    @[Provides FeatureScope]
    fun provideEditCarViewModelFactory(carsRepository: CarsRepository) =
        EditCarViewModelFactory(carsRepository = carsRepository)
}