package ru.point.profile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.profile.domain.DeleteProfileUseCase
import ru.point.profile.domain.GetUserDataUseCase
import ru.point.user.model.UserDataResponse

internal class ProfileViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val deleteProfileUseCase: DeleteProfileUseCase
) : ViewModel() {

    private val _userData = MutableStateFlow<UserDataResponse?>(null)
    val userData get() = _userData.asStateFlow()

    private val _deleteProfileEvent = MutableSharedFlow<Unit>(replay = 1)
    val deleteProfileEvent get() = _deleteProfileEvent.asSharedFlow()

    init {
        refreshUserData()
    }

    fun refreshUserData() {
        viewModelScope.launch {
            _userData.value = getUserDataUseCase()
        }
    }

    fun deleteUser() {
        viewModelScope.launch {
            deleteProfileUseCase.invoke().onSuccess {
                _deleteProfileEvent.emit(Unit)
            }
        }
    }
}