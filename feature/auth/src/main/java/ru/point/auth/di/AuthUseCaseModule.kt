package ru.point.auth.di

import dagger.Module
import dagger.Provides
import ru.point.auth.data.AuthRepository
import ru.point.auth.domain.LoginUseCase
import ru.point.auth.domain.RegisterUseCase
import ru.point.core.di.FeatureScope

@Module
class AuthUseCaseModule {

    @[Provides FeatureScope]
    fun provideLoginUseCase(authRepository: AuthRepository) =
        LoginUseCase(authRepository = authRepository)

    @[Provides FeatureScope]
    fun provideRegisterUseCase(authRepository: AuthRepository) =
        RegisterUseCase(authRepository = authRepository)
}