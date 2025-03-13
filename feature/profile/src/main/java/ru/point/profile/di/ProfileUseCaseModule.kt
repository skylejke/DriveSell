package ru.point.profile.di

import dagger.Module
import dagger.Provides
import ru.point.core.di.FeatureScope
import ru.point.profile.domain.GetUserDataUseCase
import ru.point.user.repository.UserRepository

@Module
internal class ProfileUseCaseModule {

    @[Provides FeatureScope]
    fun provideGetUserDataUseCase(userRepository: UserRepository) =
        GetUserDataUseCase(userRepository = userRepository)
}