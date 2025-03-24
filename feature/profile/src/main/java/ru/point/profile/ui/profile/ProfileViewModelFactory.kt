package ru.point.profile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.profile.domain.DeleteProfileUseCase
import ru.point.profile.domain.GetUserDataUseCase

@Suppress("UNCHECKED_CAST")
internal class ProfileViewModelFactory(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ProfileViewModel(
            getUserDataUseCase = getUserDataUseCase,
            deleteProfileUseCase = deleteProfileUseCase
        ) as T
}