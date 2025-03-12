package ru.point.auth.di

import dagger.Module
import dagger.Provides
import ru.point.auth.domain.LoginUseCase
import ru.point.auth.domain.RegisterUseCase
import ru.point.auth.ui.login.LoginViewModelFactory
import ru.point.auth.ui.register.RegisterViewModelFactory
import ru.point.core.di.FeatureScope
import ru.point.user.repository.UserRepository

@Module
internal class AuthViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideLoginViewModelFactory(
        loginUseCase: LoginUseCase,
        userRepository: UserRepository
    ) = LoginViewModelFactory(
        loginUseCase = loginUseCase,
        userRepository = userRepository
    )

    @[Provides FeatureScope]
    fun provideRegisterViewModelFactory(
        registerUseCase: RegisterUseCase,
    ) = RegisterViewModelFactory(registerUseCase = registerUseCase)
}