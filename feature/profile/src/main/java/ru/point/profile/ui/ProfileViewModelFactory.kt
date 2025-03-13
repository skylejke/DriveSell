package ru.point.profile.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.profile.domain.GetUserDataUseCase

@Suppress("UNCHECKED_CAST")
internal class ProfileViewModelFactory(private val getUserDataUseCase: GetUserDataUseCase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ProfileViewModel(getUserDataUseCase = getUserDataUseCase) as T
}