package ru.point.auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.point.auth.domain.RegisterUseCase

class RegisterViewModelFactory(val registerUseCase: RegisterUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
        RegisterViewModel(registerUseCase = registerUseCase) as T
}