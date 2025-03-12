package ru.point.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.auth.domain.LoginUseCase
import ru.point.user.model.LoginRequest
import ru.point.user.repository.UserRepository

internal class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _isAuthorized = MutableStateFlow<Boolean?>(null)
    val isAuthorized get() = _isAuthorized.asStateFlow()

    init {
        viewModelScope.launch {
            _isAuthorized.value = userRepository.isAuthorized()
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isAuthorized.value = loginUseCase.invoke(
                loginRequest = LoginRequest(
                    username = username,
                    password = password
                )
            )
        }
    }
}