package ru.point.comparisons.di

import dagger.Component
import ru.point.cars.di.CarsRepositoryModule
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule
import ru.point.comparisons.ui.ComparisonsFragment

@[FeatureScope Component(
    modules = [
        TokenStorageModule::class,
        CarsRepositoryModule::class
    ],
    dependencies = [FeatureDeps::class]
)]
internal interface ComparisonsComponent {
    fun inject(comparisonsFragment: ComparisonsFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): ComparisonsComponent
    }
}