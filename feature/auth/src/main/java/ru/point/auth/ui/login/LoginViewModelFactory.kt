package ru.point.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.auth.domain.LoginUseCase

class LoginViewModelFactory(val loginUseCase: LoginUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        LoginViewModel(loginUseCase = loginUseCase) as T
}