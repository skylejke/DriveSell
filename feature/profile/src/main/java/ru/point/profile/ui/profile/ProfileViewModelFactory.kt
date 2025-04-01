package ru.point.profile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.user.repository.UserRepository

@Suppress("UNCHECKED_CAST")
internal class ProfileViewModelFactory(
    private val userRepository: UserRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ProfileViewModel(userRepository = userRepository) as T
}