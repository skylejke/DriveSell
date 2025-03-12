package ru.point.auth.di

import dagger.Module
import dagger.Provides
import ru.point.auth.domain.LoginUseCase
import ru.point.auth.domain.RegisterUseCase
import ru.point.core.di.FeatureScope
import ru.point.user.repository.UserRepository

@Module
internal class AuthUseCaseModule {

    @[Provides FeatureScope]
    fun provideLoginUseCase(userRepository: UserRepository) =
        LoginUseCase(userRepository = userRepository)

    @[Provides FeatureScope]
    fun provideRegisterUseCase(userRepository: UserRepository) =
        RegisterUseCase(userRepository = userRepository)
}