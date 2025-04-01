package ru.point.menu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.user.repository.UserRepository

@Suppress("UNCHECKED_CAST")
internal class MenuViewModelFactory(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        MenuViewModel(userRepository = userRepository) as T
}