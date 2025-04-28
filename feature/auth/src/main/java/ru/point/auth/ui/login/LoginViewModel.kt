package ru.point.auth.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.point.auth.R
import ru.point.common.model.Status
import ru.point.common.utils.ResourceProvider
import ru.point.user.model.LoginRequest
import ru.point.user.repository.UserRepository

internal class LoginViewModel(
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _loginEvent = MutableSharedFlow<Unit>(replay = 1)
    val loginEvent get() = _loginEvent.asSharedFlow()

    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError get() = _usernameError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError get() = _passwordError.asStateFlow()

    private val _status = MutableStateFlow<Status?>(null)
    val status get() = _status.asStateFlow()

    fun login(username: String, password: String) {
        _status.value = Status.Loading
        if (!validateLoginFields(username, password)) {
            _status.value = Status.Error
            return
        }
        viewModelScope.launch {
            userRepository.login(LoginRequest(username = username, password = password)).fold(
                onSuccess = {
                    _status.value = Status.Success
                    _loginEvent.emit(Unit)
                },
                onFailure = { error ->
                    _status.value = Status.Error
                    if (error is HttpException) {
                        when (error.code()) {
                            403 -> _passwordError.value = resourceProvider.getString(R.string.error_incorrect_password)
                            404 -> _usernameError.value = resourceProvider.getString(R.string.error_user_not_found)
                        }
                    }
                }
            )
        }
    }

    private fun validateLoginFields(username: String, password: String): Boolean {

        _usernameError.value = null
        _passwordError.value = null

        var valid = true

        if (username.isBlank()) {
            _usernameError.value =  resourceProvider.getString(R.string.error_username_required)
            valid = false
        }

        if (password.isBlank()) {
            _passwordError.value = resourceProvider.getString(R.string.error_password_required)
            valid = false
        }

        return valid
    }
}