package ru.point.profile.di

import dagger.Module
import dagger.Provides
import ru.point.core.di.FeatureScope
import ru.point.profile.domain.DeleteProfileUseCase
import ru.point.profile.domain.EditPasswordUseCase
import ru.point.profile.domain.EditUserDataUseCase
import ru.point.profile.domain.GetUserDataUseCase
import ru.point.user.repository.UserRepository

@Module
internal class ProfileUseCaseModule {

    @[Provides FeatureScope]
    fun provideGetUserDataUseCase(userRepository: UserRepository) =
        GetUserDataUseCase(userRepository = userRepository)

    @[Provides FeatureScope]
    fun provideEditUserDataUseCase(userRepository: UserRepository) =
        EditUserDataUseCase(userRepository = userRepository)

    @[Provides FeatureScope]
    fun provideEditPasswordUseCase(userRepository: UserRepository) =
        EditPasswordUseCase(userRepository = userRepository)

    @[Provides FeatureScope]
    fun provideDeleteProfileUseCase(userRepository: UserRepository) =
        DeleteProfileUseCase(userRepository = userRepository)
}