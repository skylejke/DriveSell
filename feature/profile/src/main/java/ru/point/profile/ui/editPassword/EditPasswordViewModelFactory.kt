package ru.point.profile.ui.editPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.user.repository.UserRepository

@Suppress("UNCHECKED_CAST")
internal class EditPasswordViewModelFactory(
    private val userRepository: UserRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        EditPasswordViewModel(userRepository = userRepository) as T
}