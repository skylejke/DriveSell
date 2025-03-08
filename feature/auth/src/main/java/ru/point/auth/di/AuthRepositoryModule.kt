package ru.point.auth.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.point.auth.data.AuthRepository
import ru.point.auth.data.AuthRepositoryImpl
import ru.point.auth.data.AuthService
import ru.point.auth.storage.AuthStorage
import ru.point.core.di.FeatureScope


@Module
internal class AuthRepositoryModule {

    @[Provides FeatureScope]
    fun provideAuthRepository(authService: AuthService, authStorage: AuthStorage): AuthRepository =
        AuthRepositoryImpl(authService = authService, authStorage = authStorage)

    @[Provides FeatureScope]
    fun provideAuthService(retrofit: Retrofit) = retrofit.create<AuthService>()
}