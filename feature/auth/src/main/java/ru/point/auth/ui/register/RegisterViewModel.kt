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
import ru.point.auth.R
import ru.point.common.ext.isValidEmail
import ru.point.common.ext.isValidPassword
import ru.point.common.ext.isValidPhoneNumber
import ru.point.common.ext.isValidUserName
import ru.point.common.model.Status
import ru.point.common.utils.ResourceProvider
import ru.point.user.model.AuthResponse
import ru.point.user.model.RegisterRequest
import ru.point.user.repository.UserRepository

internal class RegisterViewModel(
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _registerEvent = MutableSharedFlow<Unit>(replay = 1)
    val registerEvent get() = _registerEvent.asSharedFlow()

    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError get() = _usernameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError get() = _emailError.asStateFlow()

    private val _phoneNumberError = MutableStateFlow<String?>(null)
    val phoneNumberError get() = _phoneNumberError.asStateFlow()

    private val _passwordError = MutableStateFlow<String?>(null)
    val passwordError get() = _passwordError.asStateFlow()

    private val _status = MutableStateFlow<Status?>(null)
    val status get() = _status.asStateFlow()

    fun register(username: String, email: String, phoneNumber: String, password: String) {
        _status.value = Status.Loading
        if (!validateRegisterFields(username, email, phoneNumber, password)) {
            _status.value = Status.Error
            return
        }
        viewModelScope.launch {
            userRepository.register(
                RegisterRequest(
                    username = username,
                    email = email,
                    phoneNumber = phoneNumber,
                    password = password
                )
            ).fold(
                onSuccess = {
                    _status.value = Status.Success
                    _registerEvent.emit(Unit)
                },
                onFailure = { error ->
                    _status.value = Status.Error
                    if (error is HttpException && error.code() == 409) {
                        val errorBody = error.response()?.errorBody()?.string()
                        errorBody?.let {
                            val registerResponse = Json.decodeFromString<AuthResponse>(it)
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
                }
            )
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
            _usernameError.value = resourceProvider.getString(R.string.error_username_required)
            valid = false
        } else if (!username.isValidUserName()) {
            _usernameError.value = resourceProvider.getString(R.string.error_username_invalid)
            valid = false
        }

        if (email.isBlank()) {
            _emailError.value = resourceProvider.getString(R.string.error_email_required)
            valid = false
        } else if (!email.isValidEmail()) {
            _emailError.value = resourceProvider.getString(R.string.error_email_invalid)
            valid = false
        }

        if (phoneNumber.isBlank()) {
            _phoneNumberError.value = resourceProvider.getString(R.string.error_phone_required)
            valid = false
        } else if (!phoneNumber.isValidPhoneNumber()) {
            _phoneNumberError.value = resourceProvider.getString(R.string.error_phone_invalid)
            valid = false
        }

        if (password.isBlank()) {
            _passwordError.value = resourceProvider.getString(R.string.error_password_required)
            valid = false
        } else if (!password.isValidPassword()) {
            _passwordError.value = resourceProvider.getString(R.string.error_password_invalid)
            valid = false
        }

        return valid
    }
}