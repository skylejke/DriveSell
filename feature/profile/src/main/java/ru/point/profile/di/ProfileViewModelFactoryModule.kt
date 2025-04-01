package ru.point.profile.di

import dagger.Module
import dagger.Provides
import ru.point.common.di.FeatureScope
import ru.point.profile.ui.editPassword.EditPasswordViewModelFactory
import ru.point.profile.ui.editUserData.EditUserDataViewModelFactory
import ru.point.profile.ui.profile.ProfileViewModelFactory
import ru.point.user.repository.UserRepository

@Module
internal class ProfileViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideProfileViewModelFactory(userRepository: UserRepository) =
        ProfileViewModelFactory(userRepository = userRepository)

    @[Provides FeatureScope]
    fun provideEditUserDataMViewModelFactory(userRepository: UserRepository) =
        EditUserDataViewModelFactory(userRepository = userRepository)

    @[Provides FeatureScope]
    fun provideEditPasswordViewModelFactory(userRepository: UserRepository) =
        EditPasswordViewModelFactory(userRepository = userRepository)
}