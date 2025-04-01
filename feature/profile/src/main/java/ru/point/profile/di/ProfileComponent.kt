package ru.point.profile.di

import dagger.Component
import ru.point.common.di.FeatureDeps
import ru.point.common.di.FeatureScope
import ru.point.common.di.TokenStorageModule
import ru.point.profile.ui.editPassword.EditPasswordFragment
import ru.point.profile.ui.editUserData.EditUserDataFragment
import ru.point.profile.ui.profile.ProfileFragment
import ru.point.user.di.UserRepositoryModule

@[FeatureScope Component(
    modules = [
        TokenStorageModule::class,
        UserRepositoryModule::class,
        ProfileViewModelFactoryModule::class
    ],
    dependencies = [FeatureDeps::class]
)]
internal interface ProfileComponent {
    fun inject(profileFragment: ProfileFragment)
    fun inject(editUserDataFragment: EditUserDataFragment)
    fun inject(editPasswordFragment: EditPasswordFragment)

    @Component.Builder
    interface Builder {
        fun deps(featureDeps: FeatureDeps): Builder
        fun build(): ProfileComponent
    }
}