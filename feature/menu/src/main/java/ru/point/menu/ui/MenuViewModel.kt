package ru.point.menu.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.point.menu.domain.LogOutUseCase
import ru.point.user.repository.UserRepository

internal class MenuViewModel(
    private val logOutUseCase: LogOutUseCase,
    private val userRepository: UserRepository
) : ViewModel() {

    private var _isAuthorized = MutableStateFlow<Boolean?>(null)
    val isAuthorized get() = _isAuthorized.asStateFlow()

    init {
        viewModelScope.launch {
            _isAuthorized.value = userRepository.isAuthorized()
        }
    }

    fun logOut() {
        viewModelScope.launch {
            logOutUseCase()
        }
    }
}