package ru.point.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.auth.domain.LoginUseCase
import ru.point.user.repository.UserRepository

@Suppress("UNCHECKED_CAST")
internal class LoginViewModelFactory(
    private val loginUseCase: LoginUseCase,
    private val userRepository: UserRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        LoginViewModel(
            loginUseCase = loginUseCase,
            userRepository = userRepository
        ) as T
}