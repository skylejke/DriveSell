package ru.point.car_details.di

import dagger.Component
import ru.point.car_details.ui.CarDetailsFragment
import ru.point.cars.di.CarsRepositoryModule
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule

@[FeatureScope Component(
    modules = [TokenStorageModule::class, CarsRepositoryModule::class, CarDetailsViewModelFactoryModule::class],
    dependencies = [FeatureDeps::class]
)]
internal interface CarDetailsComponent {
    fun inject(carDetailsFragment: CarDetailsFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): CarDetailsComponent
    }
}