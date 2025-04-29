package ru.point.car_details.di

import dagger.BindsInstance
import dagger.Component
import ru.point.car_details.ui.CarDetailsFragment
import ru.point.cars.di.CarsRepositoryModule
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule
import ru.point.user.di.UserRepositoryModule
import javax.inject.Named

@[FeatureScope Component(
    modules = [
        TokenStorageModule::class,
        CarsRepositoryModule::class,
        UserRepositoryModule::class
    ],
    dependencies = [FeatureDeps::class]
)]
internal interface CarDetailsComponent {
    fun inject(carDetailsFragment: CarDetailsFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun adId(@Named("adId") adId: String): Builder

        @BindsInstance
        fun userId(@Named("userId") userId: String): Builder

        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): CarDetailsComponent
    }
}