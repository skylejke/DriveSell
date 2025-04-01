package ru.point.user.repository

import ru.point.common.storage.TokenStorage
import ru.point.user.model.EditUserDataRequest
import ru.point.user.model.EditUserPasswordRequest
import ru.point.user.model.LoginRequest
import ru.point.user.model.RegisterRequest
import ru.point.user.service.UserService


class UserRepositoryImpl(
    private val userService: UserService,
    private val tokenStorage: TokenStorage
) : UserRepository {

    override suspend fun getUserId() = tokenStorage.getUserId()

    override suspend fun login(loginRequest: LoginRequest) =
        userService.login(loginRequest).onSuccess {
            tokenStorage.token = it.token?.token
        }

    override suspend fun register(registerRequest: RegisterRequest) =
        userService.register(registerRequest).onSuccess {
            tokenStorage.token = it.token?.token
        }

    override suspend fun logOut() {
        tokenStorage.token = null
    }

    override suspend fun isAuthorized() = tokenStorage.token != null

    override suspend fun getUserData() =
        userService.getUserData(userId = tokenStorage.getUserId()).getOrNull()

    override suspend fun editUserData(editUserDataRequest: EditUserDataRequest) =
        userService.editUserData(
            userId = tokenStorage.getUserId(),
            editUserDataRequest = editUserDataRequest
        )

    override suspend fun editPassword(editUserPasswordRequest: EditUserPasswordRequest) =
        userService.editPassword(
            userId = tokenStorage.getUserId(),
            editUserPasswordRequest = editUserPasswordRequest
        )

    override suspend fun deleteProfile() =
        userService.deleteProfile(userId = tokenStorage.getUserId()).onSuccess {
            tokenStorage.token = null
        }
}