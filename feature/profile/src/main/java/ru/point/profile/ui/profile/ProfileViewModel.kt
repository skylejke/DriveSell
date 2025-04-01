package ru.point.profile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.user.model.UserData
import ru.point.user.repository.UserRepository

internal class ProfileViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userData = MutableStateFlow<UserData?>(null)
    val userData get() = _userData.asStateFlow()

    private val _deleteProfileEvent = MutableSharedFlow<Unit>(replay = 1)
    val deleteProfileEvent get() = _deleteProfileEvent.asSharedFlow()

    init {
        refreshUserData()
    }

    fun refreshUserData() {
        viewModelScope.launch {
            _userData.value = userRepository.getUserData()
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            userRepository.deleteProfile().onSuccess {
                _deleteProfileEvent.emit(Unit)
            }
        }
    }
}