package ru.point.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.point.user.model.LoginRequest
import ru.point.user.repository.UserRepository

internal class LoginViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<Unit>(replay = 1)
    val loginEvent get() = _loginEvent.asSharedFlow()

    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError get() = _usernameError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError get() = _passwordError.asStateFlow()

    init {
        viewModelScope.launch {
            if (userRepository.isAuthorized()) _loginEvent.emit(Unit)
        }
    }

    fun login(username: String, password: String) {
        if (!validateLoginFields(username, password)) return
        viewModelScope.launch {
            userRepository.login(LoginRequest(username = username, password = password)).fold(
                onSuccess = {
                    _loginEvent.emit(Unit)
                },
                onFailure = { error ->
                    if (error is HttpException) {
                        when (error.code()) {
                            403 -> _passwordError.value = "Password is incorrect"
                            404 -> _usernameError.value = "User does not exist"
                        }
                    }
                }
            )
        }
    }

    private fun validateLoginFields(
        username: String,
        password: String
    ): Boolean {

        _usernameError.value = null
        _passwordError.value = null

        var valid = true

        if (username.isBlank()) {
            _usernameError.value = "Username must be provided"
            valid = false
        }

        if (password.isBlank()) {
            _passwordError.value = "Password must be provided"
            valid = false
        }

        return valid
    }
}