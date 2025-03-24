package ru.point.user.repository

import com.auth0.jwt.JWT
import ru.point.user.model.EditUserDataRequest
import ru.point.user.model.EditUserPasswordRequest
import ru.point.user.model.LoginRequest
import ru.point.user.model.RegisterRequest
import ru.point.user.service.UserService
import ru.point.user.storage.TokenStorage

class UserRepositoryImpl(
    private val userService: UserService,
    private val tokenStorage: TokenStorage
) : UserRepository {

    override suspend fun login(loginRequest: LoginRequest) =
        userService.login(loginRequest).map { response ->
            tokenStorage.token = response.token?.token
            response
        }

    override suspend fun register(registerRequest: RegisterRequest) =
        userService.register(registerRequest).map { response ->
            tokenStorage.token = response.token?.token
            response
        }

    override suspend fun logOut() {
        tokenStorage.token = null
    }

    override suspend fun isAuthorized() = tokenStorage.token != null

    override suspend fun getUserData() =
        userService.getUserData(userId = JWT.decode(tokenStorage.token).subject).getOrNull()

    override suspend fun editUserData(editUserDataRequest: EditUserDataRequest) =
        userService.editUserData(
            userId = JWT.decode(tokenStorage.token).subject,
            editUserDataRequest = editUserDataRequest
        )

    override suspend fun editPassword(editUserPasswordRequest: EditUserPasswordRequest) =
        userService.editPassword(
            userId = JWT.decode(tokenStorage.token).subject,
            editUserPasswordRequest = editUserPasswordRequest
        )

    override suspend fun deleteProfile() =
        userService.deleteProfile(userId = JWT.decode(tokenStorage.token).subject)
}