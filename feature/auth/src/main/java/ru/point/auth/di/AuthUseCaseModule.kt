package ru.point.auth.di

import dagger.Module
import dagger.Provides
import ru.point.auth.data.AuthRepository
import ru.point.auth.domain.LoginUseCase
import ru.point.auth.domain.LoginWithTokenUseCase
import ru.point.auth.domain.RegisterUseCase
import ru.point.auth.storage.AuthStorage
import ru.point.core.di.FeatureScope

@Module
internal class AuthUseCaseModule {

    @[Provides FeatureScope]
    fun provideLoginUseCase(authRepository: AuthRepository, authStorage: AuthStorage) =
        LoginUseCase(authRepository = authRepository, authStorage = authStorage)

    @[Provides FeatureScope]
    fun provideLoginWithTokenUseCase(authRepository: AuthRepository) =
        LoginWithTokenUseCase(authRepository = authRepository)

    @[Provides FeatureScope]
    fun provideRegisterUseCase(authRepository: AuthRepository, authStorage: AuthStorage) =
        RegisterUseCase(authRepository = authRepository, authStorage = authStorage)
}