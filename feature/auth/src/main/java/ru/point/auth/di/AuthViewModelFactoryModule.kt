package ru.point.auth.di

import dagger.Module
import dagger.Provides
import ru.point.auth.ui.login.LoginViewModelFactory
import ru.point.auth.ui.register.RegisterViewModelFactory
import ru.point.common.di.FeatureScope
import ru.point.user.repository.UserRepository

@Module
internal class AuthViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideLoginViewModelFactory(userRepository: UserRepository) =
        LoginViewModelFactory(userRepository = userRepository)

    @[Provides FeatureScope]
    fun provideRegisterViewModelFactory(userRepository: UserRepository) =
        RegisterViewModelFactory(userRepository = userRepository)
}