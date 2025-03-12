package ru.point.menu.di

import dagger.Module
import dagger.Provides
import ru.point.core.di.FeatureScope
import ru.point.menu.domain.LogOutUseCase
import ru.point.menu.ui.MenuViewModelFactory
import ru.point.user.repository.UserRepository

@Module
internal class MenuViewModelFactoryModule {
    @[Provides FeatureScope]
    fun provideMenuViewModelFactory(logOutUseCase: LogOutUseCase, userRepository: UserRepository) =
        MenuViewModelFactory(logOutUseCase = logOutUseCase, userRepository = userRepository)
}