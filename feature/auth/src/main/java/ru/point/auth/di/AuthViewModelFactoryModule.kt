package ru.point.auth.di

import dagger.Module
import dagger.Provides
import ru.point.auth.domain.LoginUseCase
import ru.point.auth.domain.RegisterUseCase
import ru.point.auth.ui.login.LoginViewModelFactory
import ru.point.auth.ui.register.RegisterViewModelFactory
import ru.point.core.di.FeatureScope

@Module
class AuthViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideLoginViewModelFactory(loginUseCase: LoginUseCase) =
        LoginViewModelFactory(loginUseCase = loginUseCase)

    @[Provides FeatureScope]
    fun provideRegisterViewModelFactory(registerUseCase: RegisterUseCase) =
        RegisterViewModelFactory(registerUseCase = registerUseCase)
}