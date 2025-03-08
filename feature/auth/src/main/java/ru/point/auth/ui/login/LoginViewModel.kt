package ru.point.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.auth.data.model.LoginRequest
import ru.point.auth.domain.LoginUseCase
import ru.point.auth.domain.LoginWithTokenUseCase

internal class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loginWithTokenUseCase: LoginWithTokenUseCase
) : ViewModel() {

    private var _loginState = MutableStateFlow<Boolean?>(null)
    val loginState get() = _loginState.asStateFlow()

    init {
        viewModelScope.launch {
            _loginState.value = loginWithTokenUseCase.invoke()
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = loginUseCase.invoke(
                loginRequest = LoginRequest(
                    username = username,
                    password = password
                )
            )
        }
    }
}