package ru.point.profile.di

import dagger.Module
import dagger.Provides
import ru.point.common.di.FeatureScope
import ru.point.common.utils.ResourceProvider
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
    fun provideEditUserDataMViewModelFactory(userRepository: UserRepository, resourceProvider: ResourceProvider) =
        EditUserDataViewModelFactory(userRepository = userRepository, resourceProvider = resourceProvider)

    @[Provides FeatureScope]
    fun provideEditPasswordViewModelFactory(userRepository: UserRepository, resourceProvider: ResourceProvider) =
        EditPasswordViewModelFactory(userRepository = userRepository, resourceProvider = resourceProvider)
}