package ru.point.profile.ui.editUserData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import retrofit2.HttpException
import ru.point.common.ext.isValidEmail
import ru.point.common.ext.isValidPhoneNumber
import ru.point.common.ext.isValidUserName
import ru.point.common.model.ResponseMessage
import ru.point.user.model.EditUserDataRequest
import ru.point.user.model.UserData
import ru.point.user.repository.UserRepository

internal class EditUserDataViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData get() = _userData.asStateFlow()

    private val _usernameError = MutableStateFlow<String?>(null)
    val usernameError get() = _usernameError.asStateFlow()

    private val _emailError = MutableStateFlow<String?>(null)
    val emailError get() = _emailError.asStateFlow()

    private val _phoneNumberError = MutableStateFlow<String?>(null)
    val phoneNumberError get() = _phoneNumberError.asStateFlow()

    private val _userDataChangedEvent = MutableSharedFlow<Unit>(replay = 1)
    val userDataChangedEvent get() = _userDataChangedEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            _userData.value = userRepository.getUserData()
        }
    }

    fun editUserData(username: String?, email: String?, phoneNumber: String?) {
        val currentUserData = _userData.value ?: return

        val updatedUsername =
            if (!username.isNullOrBlank() && username != currentUserData.username) username else null
        val updatedEmail =
            if (!email.isNullOrBlank() && email != currentUserData.email) email else null
        val updatedPhoneNumber =
            if (!phoneNumber.isNullOrBlank() && phoneNumber != currentUserData.phoneNumber) phoneNumber else null

        if (updatedUsername == null && updatedEmail == null && updatedPhoneNumber == null) {
            viewModelScope.launch { _userDataChangedEvent.emit(Unit) }
            return
        }

        if (!validateUserDataFields(updatedUsername, updatedEmail, updatedPhoneNumber)) return

        viewModelScope.launch {
            userRepository.editUserData(
                EditUserDataRequest(
                    username = updatedUsername,
                    email = updatedEmail,
                    phoneNumber = updatedPhoneNumber
                )
            ).fold(
                onSuccess = {
                    _userDataChangedEvent.emit(Unit)
                },
                onFailure = { error ->
                    if (error is HttpException && error.code() == 409) {
                        val errorBody = error.response()?.errorBody()?.string()
                        errorBody?.let {
                            val editDataResponse =
                                Json.decodeFromString<ResponseMessage>(it)

                            when (editDataResponse.message) {
                                "Username is taken" -> _usernameError.value =
                                    editDataResponse.message

                                "Email is taken" -> _emailError.value =
                                    editDataResponse.message

                                "Phone number is taken" -> _phoneNumberError.value =
                                    editDataResponse.message
                            }
                        }
                    }
                }
            )
        }
    }

    private fun validateUserDataFields(
        username: String?,
        email: String?,
        phoneNumber: String?
    ): Boolean {
        _usernameError.value = null
        _emailError.value = null
        _phoneNumberError.value = null

        var valid = true

        username?.let {
            if (!it.isValidUserName()) {
                _usernameError.value = "Username is invalid"
                valid = false
            }
        }
        email?.let {
            if (!it.isValidEmail()) {
                _emailError.value = "Email is invalid"
                valid = false
            }
        }
        phoneNumber?.let {
            if (!it.isValidPhoneNumber()) {
                _phoneNumberError.value = "Phone number is invalid"
                valid = false
            }
        }
        return valid
    }
}
