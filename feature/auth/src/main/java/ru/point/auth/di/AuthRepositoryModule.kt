package ru.point.auth.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.create
import ru.point.auth.data.AuthRepository
import ru.point.auth.data.AuthRepositoryImpl
import ru.point.auth.data.AuthService
import ru.point.core.di.FeatureScope


@Module
class AuthRepositoryModule {

    @[Provides FeatureScope]
    fun provideAuthRepository(authService: AuthService): AuthRepository = AuthRepositoryImpl(authService)

    @[Provides FeatureScope]
    fun provideAuthService(retrofit: Retrofit) = retrofit.create<AuthService>()
}