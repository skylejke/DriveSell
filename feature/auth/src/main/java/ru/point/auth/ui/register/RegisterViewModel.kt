package ru.point.auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.auth.domain.RegisterUseCase
import ru.point.user.model.RegisterRequest

internal class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private var _registerState = MutableStateFlow<Boolean?>(null)
    val registerState get() = _registerState.asStateFlow()

    fun register(username: String, email: String, phoneNumber: String, password: String) {
        viewModelScope.launch {
            _registerState.value = registerUseCase.invoke(
                registerRequest = RegisterRequest(
                    username = username,
                    email = email,
                    phoneNumber = phoneNumber,
                    password = password
                )
            )
        }
    }
}