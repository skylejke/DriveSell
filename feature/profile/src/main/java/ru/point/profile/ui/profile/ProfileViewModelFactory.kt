package ru.point.profile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.user.repository.UserRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
internal class ProfileViewModelFactory @Inject constructor(
    private val userRepository: UserRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        ProfileViewModel(userRepository = userRepository) as T
}