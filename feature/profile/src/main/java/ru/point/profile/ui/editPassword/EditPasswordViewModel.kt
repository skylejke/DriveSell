package ru.point.profile.ui.editPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.point.common.ext.isValidPassword
import ru.point.user.model.EditUserPasswordRequest
import ru.point.user.repository.UserRepository

internal class EditPasswordViewModel(
    private val userRepository: UserRepository,
) :
    ViewModel() {

    private val _oldPasswordError = MutableStateFlow<String?>(null)
    val oldPasswordError get() = _oldPasswordError.asStateFlow()

    private val _newPasswordError = MutableStateFlow<String?>(null)
    val newPasswordError get() = _newPasswordError.asStateFlow()

    private val _confirmNewPasswordError = MutableStateFlow<String?>(null)
    val confirmNewPasswordError get() = _confirmNewPasswordError.asStateFlow()

    private val _passwordChangedEvent = MutableSharedFlow<Unit>(replay = 1)
    val passwordChangedEvent = _passwordChangedEvent.asSharedFlow()

    fun editPassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) {
        if (!validatePasswordFields(oldPassword, newPassword, confirmNewPassword)) return
        viewModelScope.launch {
            userRepository.editPassword(
                EditUserPasswordRequest(
                    oldPassword = oldPassword,
                    newPassword = newPassword,
                    confirmNewPassword = confirmNewPassword
                )
            ).fold(
                onSuccess = {
                    _passwordChangedEvent.emit(Unit)
                },
                onFailure = { error ->
                    if (error is HttpException) {
                        when (error.code()) {
                            403 -> _oldPasswordError.value = "Current password is incorrect"
                        }
                    }
                }
            )
        }
    }

    private fun validatePasswordFields(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ): Boolean {
        _oldPasswordError.value = null
        _newPasswordError.value = null
        _confirmNewPasswordError.value = null

        var valid = true

        if (oldPassword.isBlank()) {
            _oldPasswordError.value = "Old password must be provided"
            valid = false
        }

        if (newPassword.isBlank()) {
            _newPasswordError.value = "New password must be provided"
            valid = false
        } else if (!newPassword.isValidPassword()) {
            _newPasswordError.value = "New password is invalid"
            valid = false
        }

        if (confirmNewPassword.isBlank()) {
            _confirmNewPasswordError.value = "Confirm new password must be provided"
            valid = false
        } else if (newPassword != confirmNewPassword) {
            _confirmNewPasswordError.value = "New password and confirmation do not match"
            valid = false
        }

        return valid
    }
}