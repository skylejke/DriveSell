package ru.point.menu.di

import dagger.Module
import dagger.Provides
import ru.point.common.di.FeatureScope
import ru.point.menu.ui.MenuViewModelFactory
import ru.point.user.repository.UserRepository

@Module
internal class MenuViewModelFactoryModule {
    @[Provides FeatureScope]
    fun provideMenuViewModelFactory(userRepository: UserRepository) =
        MenuViewModelFactory(userRepository = userRepository)
}