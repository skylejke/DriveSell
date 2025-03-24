package ru.point.auth.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import ru.point.auth.domain.RegisterUseCase
import ru.point.core.ext.isValidEmail
import ru.point.core.ext.isValidPassword
import ru.point.core.ext.isValidPhoneNumber
import ru.point.core.ext.isValidUserName
import ru.point.user.model.RegisterRequest
import ru.point.user.model.RegisterResponse

internal class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
) : ViewModel() {

    private val _registerEvent = MutableSharedFlow<Unit>()
    val registerEvent get() = _registerEvent.asSharedFlow()

    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError get() = _usernameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError get() = _emailError.asStateFlow()

    private val _phoneNumberError = MutableStateFlow<String?>(null)
    val phoneNumberError get() = _phoneNumberError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError get() = _passwordError.asStateFlow()

    fun register(username: String, email: String, phoneNumber: String, password: String) {
        if (!validateRegisterFields(username, email, phoneNumber, password)) return
        viewModelScope.launch {
            registerUseCase.invoke(
                registerRequest = RegisterRequest(
                    username = username,
                    email = email,
                    phoneNumber = phoneNumber,
                    password = password
                )
            ).fold(
                onSuccess = {
                    _registerEvent.emit(Unit)
                },
                onFailure = { error ->
                    if (error is HttpException && error.code() == 409) {
                        val errorBody = error.response()?.errorBody()?.string()
                        errorBody?.let {
                            val registerResponse = Json.decodeFromString<RegisterResponse>(it)
                            when (registerResponse.message) {
                                "Username is taken" -> _usernameError.value =
                                    registerResponse.message

                                "Email is taken" -> _emailError.value =
                                    registerResponse.message

                                "Phone number is taken" -> _phoneNumberError.value =
                                    registerResponse.message
                            }
                        }
                    }
                })
        }
    }

    private fun validateRegisterFields(
        username: String,
        email: String,
        phoneNumber: String,
        password: String
    ): Boolean {
        _usernameError.value = null
        _emailError.value = null
        _phoneNumberError.value = null
        _passwordError.value = null

        var valid = true


        if (username.isBlank()) {
            _usernameError.value = "Username must be provided"
            valid = false
        } else if (!username.isValidUserName()) {
            _usernameError.value = "Username is invalid"
            valid = false
        }

        if (email.isBlank()) {
            _emailError.value = "Email must be provided"
            valid = false
        } else if (!email.isValidEmail()) {
            _emailError.value = "Email is invalid"
            valid = false
        }

        if (phoneNumber.isBlank()) {
            _phoneNumberError.value = "Phone number must be provided"
            valid = false
        } else if (!phoneNumber.isValidPhoneNumber()) {
            _passwordError.value = "Phone number is invalid"
            valid = false
        }

        if (password.isBlank()) {
            _passwordError.value = "Password must be provided"
            valid = false
        } else if (!password.isValidPassword()) {
            _passwordError.value = "Password is invalid"
            valid = false
        }

        return valid
    }
}