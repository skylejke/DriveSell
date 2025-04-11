package ru.point.add_car.di

import dagger.Component
import ru.point.add_car.ui.AddCarFragment
import ru.point.cars.di.CarsRepositoryModule
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule
import ru.point.user.di.UserRepositoryModule

@[FeatureScope Component(
    modules = [
        TokenStorageModule::class,
        UserRepositoryModule::class,
        CarsRepositoryModule::class,
        AddCarViewModelFactoryModule::class
    ],
    dependencies = [FeatureDeps::class]
)]
internal interface AddCarComponent {
    fun inject(addCarFragment: AddCarFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): AddCarComponent
    }
}