package ru.point.profile.di

import dagger.Module
import dagger.Provides
import ru.point.core.di.FeatureScope
import ru.point.profile.domain.GetUserDataUseCase
import ru.point.profile.ui.ProfileViewModelFactory

@Module
internal class ProfileViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideProfileViewModelFactory(getUserDataUseCase: GetUserDataUseCase) =
        ProfileViewModelFactory(getUserDataUseCase = getUserDataUseCase)
}