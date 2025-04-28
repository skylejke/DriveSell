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
import ru.point.common.model.Status
import ru.point.common.utils.ResourceProvider
import ru.point.profile.R
import ru.point.user.model.EditUserPasswordRequest
import ru.point.user.repository.UserRepository

internal class EditPasswordViewModel(
    private val userRepository: UserRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    private val _oldPasswordError = MutableStateFlow<String?>(null)
    val oldPasswordError get() = _oldPasswordError.asStateFlow()

    private val _newPasswordError = MutableStateFlow<String?>(null)
    val newPasswordError get() = _newPasswordError.asStateFlow()

    private val _confirmNewPasswordError = MutableStateFlow<String?>(null)
    val confirmNewPasswordError get() = _confirmNewPasswordError.asStateFlow()

    private val _passwordChangedEvent = MutableSharedFlow<Unit>(replay = 1)
    val passwordChangedEvent = _passwordChangedEvent.asSharedFlow()

    private val _status = MutableStateFlow<Status?>(null)
    val status get() = _status.asStateFlow()

    fun editPassword(
        oldPassword: String,
        newPassword: String,
        confirmNewPassword: String
    ) {
        _status.value = Status.Loading
        if (!validatePasswordFields(oldPassword, newPassword, confirmNewPassword)) {
            _status.value = Status.Error
            return
        }
        viewModelScope.launch {
            userRepository.editPassword(
                EditUserPasswordRequest(
                    oldPassword = oldPassword,
                    newPassword = newPassword,
                    confirmNewPassword = confirmNewPassword
                )
            ).fold(
                onSuccess = {
                    _status.value = Status.Success
                    _passwordChangedEvent.emit(Unit)
                },
                onFailure = { error ->
                    _status.value = Status.Error
                    if (error is HttpException) {
                        when (error.code()) {
                            403 -> _oldPasswordError.value =
                                resourceProvider.getString(R.string.error_current_password_incorrect)
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
            _oldPasswordError.value = resourceProvider.getString(R.string.error_old_password_required)
            valid = false
        }

        if (newPassword.isBlank()) {
            _newPasswordError.value = resourceProvider.getString(R.string.error_new_password_required)
            valid = false
        } else if (!newPassword.isValidPassword()) {
            _newPasswordError.value = resourceProvider.getString(R.string.error_new_password_invalid)
            valid = false
        }

        if (confirmNewPassword.isBlank()) {
            _confirmNewPasswordError.value = resourceProvider.getString(R.string.error_confirm_new_password_required)
            valid = false
        } else if (newPassword != confirmNewPassword) {
            _confirmNewPasswordError.value = resourceProvider.getString(R.string.error_passwords_do_not_match)
            valid = false
        }

        return valid
    }
}