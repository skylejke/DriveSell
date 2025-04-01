package ru.point.auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.user.repository.UserRepository

@Suppress("UNCHECKED_CAST")
internal class RegisterViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        RegisterViewModel(userRepository = userRepository) as T
}