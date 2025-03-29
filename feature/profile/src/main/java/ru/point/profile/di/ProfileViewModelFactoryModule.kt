package ru.point.profile.di

import dagger.Module
import dagger.Provides
import ru.point.common.di.FeatureScope
import ru.point.profile.domain.DeleteProfileUseCase
import ru.point.profile.domain.EditPasswordUseCase
import ru.point.profile.domain.EditUserDataUseCase
import ru.point.profile.domain.GetUserDataUseCase
import ru.point.profile.ui.editPassword.EditPasswordViewModelFactory
import ru.point.profile.ui.editUserData.EditUserDataViewModelFactory
import ru.point.profile.ui.profile.ProfileViewModelFactory

@Module
internal class ProfileViewModelFactoryModule {

    @[Provides FeatureScope]
    fun provideProfileViewModelFactory(
        getUserDataUseCase: GetUserDataUseCase,
        deleteProfileUseCase: DeleteProfileUseCase
    ) =
        ProfileViewModelFactory(
            getUserDataUseCase = getUserDataUseCase,
            deleteProfileUseCase = deleteProfileUseCase
        )

    @[Provides FeatureScope]
    fun provideEditUserDataMViewModelFactory(
        getUserDataUseCase: GetUserDataUseCase,
        editUserDataUseCase: EditUserDataUseCase
    ) =
        EditUserDataViewModelFactory(
            getUserDataUseCase = getUserDataUseCase,
            editUserDataUseCase = editUserDataUseCase
        )

    @[Provides FeatureScope]
    fun provideEditPasswordViewModelFactory(editPasswordUseCase: EditPasswordUseCase) =
        EditPasswordViewModelFactory(editPasswordUseCase = editPasswordUseCase)
}