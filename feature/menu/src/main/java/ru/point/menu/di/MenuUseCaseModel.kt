package ru.point.menu.di

import dagger.Module
import dagger.Provides
import ru.point.common.di.FeatureScope
import ru.point.menu.domain.LogOutUseCase
import ru.point.user.repository.UserRepository

@Module
internal class MenuUseCaseModel {
    @[Provides FeatureScope]
    fun provideLogOutUseCase(userRepository: UserRepository) =
        LogOutUseCase(userRepository = userRepository)
}