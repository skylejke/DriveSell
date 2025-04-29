package ru.point.users_ads.di

import dagger.Component
import ru.point.cars.di.CarsRepositoryModule
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule
import ru.point.users_ads.ui.UsersAdsFragment

@[FeatureScope Component(
    modules = [TokenStorageModule::class, CarsRepositoryModule::class],
    dependencies = [FeatureDeps::class]
)]
internal interface UsersAdsComponent {
    fun inject(usersAdsFragment: UsersAdsFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): UsersAdsComponent
    }
}