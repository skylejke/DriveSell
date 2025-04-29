package ru.point.menu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.user.repository.UserRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
internal class MenuViewModelFactory @Inject constructor(
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        MenuViewModel(userRepository = userRepository) as T
}