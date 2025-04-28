package ru.point.auth.di

import dagger.Module
import dagger.Provides
import ru.point.auth.ui.login.LoginViewModelFactory
import ru.point.auth.ui.register.RegisterViewModelFactory
import ru.point.common.di.FeatureScope
import ru.point.common.utils.ResourceProvider
import ru.point.user.repository.UserRepository

@Module
internal class AuthViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideLoginViewModelFactory(userRepository: UserRepository, resourceProvider: ResourceProvider) =
        LoginViewModelFactory(userRepository = userRepository, resourceProvider = resourceProvider)

    @[Provides FeatureScope]
    fun provideRegisterViewModelFactory(userRepository: UserRepository, resourceProvider: ResourceProvider) =
        RegisterViewModelFactory(userRepository = userRepository, resourceProvider = resourceProvider)
}