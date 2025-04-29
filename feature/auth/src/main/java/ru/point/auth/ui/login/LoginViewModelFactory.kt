package ru.point.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.common.utils.ResourceProvider
import ru.point.user.repository.UserRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
internal class LoginViewModelFactory @Inject constructor(
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        LoginViewModel(
            userRepository = userRepository,
            resourceProvider = resourceProvider
        ) as T
}