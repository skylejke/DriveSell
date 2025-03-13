package ru.point.profile.di

import dagger.Component
import ru.point.core.di.FeatureDeps
import ru.point.core.di.FeatureScope
import ru.point.profile.ui.ProfileFragment
import ru.point.user.di.TokenStorageModule
import ru.point.user.di.UserRepositoryModule

@[FeatureScope Component(
    modules = [
        TokenStorageModule::class,
        UserRepositoryModule::class,
        ProfileUseCaseModule::class,
        ProfileViewModelFactoryModule::class
    ],
    dependencies = [FeatureDeps::class]
)]
internal interface ProfileComponent {
    fun inject(profileFragment: ProfileFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): ProfileComponent
    }
}