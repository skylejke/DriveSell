package ru.point.user.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.point.common.storage.TokenStorage
import ru.point.user.model.EditUserDataRequest
import ru.point.user.model.EditUserPasswordRequest
import ru.point.user.model.LoginRequest
import ru.point.user.model.RegisterRequest
import ru.point.user.service.UserService


class UserRepositoryImpl(
    private val userService: UserService,
    private val tokenStorage: TokenStorage,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {

    override suspend fun getUserId() = withContext(dispatcher) {
        tokenStorage.getUserId()
    }

    override suspend fun login(loginRequest: LoginRequest) = withContext(dispatcher) {
        userService.login(loginRequest).onSuccess {
            tokenStorage.setToken(token = it.token?.token)
        }
    }

    override suspend fun register(registerRequest: RegisterRequest) = withContext(dispatcher) {
        userService.register(registerRequest).onSuccess {
            tokenStorage.setToken(token = it.token?.token)
        }
    }

    override suspend fun logOut() = withContext(dispatcher) {
        tokenStorage.setToken(token = null)
    }

    override suspend fun isAuthorized() = withContext(dispatcher) {
        tokenStorage.tokenFlow.first() != null
    }

    override suspend fun getUserData() = withContext(dispatcher) {
        userService.getUserData(userId = tokenStorage.getUserId())
    }

    override suspend fun editUserData(editUserDataRequest: EditUserDataRequest) = withContext(dispatcher) {
        userService.editUserData(userId = tokenStorage.getUserId(), editUserDataRequest = editUserDataRequest)
    }

    override suspend fun editPassword(editUserPasswordRequest: EditUserPasswordRequest) = withContext(dispatcher) {
        userService.editPassword(userId = tokenStorage.getUserId(), editUserPasswordRequest = editUserPasswordRequest)
    }

    override suspend fun deleteProfile() = withContext(dispatcher) {
        userService.deleteProfile(userId = tokenStorage.getUserId()).onSuccess {
            tokenStorage.setToken(token = null)
        }
    }
}