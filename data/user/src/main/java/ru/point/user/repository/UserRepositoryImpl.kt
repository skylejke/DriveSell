package ru.point.user.repository

import com.auth0.jwt.JWT
import ru.point.user.model.LoginRequest
import ru.point.user.model.RegisterRequest
import ru.point.user.service.UserService
import ru.point.user.storage.TokenStorage

class UserRepositoryImpl(
    private val userService: UserService,
    private val tokenStorage: TokenStorage
) : UserRepository {
    override suspend fun login(loginRequest: LoginRequest) =
        userService.login(loginRequest).fold(onSuccess = { token ->
            tokenStorage.token = token.token
            true
        }, onFailure = { false })

    override suspend fun register(registerRequest: RegisterRequest) =
        userService.register(registerRequest).fold(onSuccess = { token ->
            tokenStorage.token = token.token
            true
        }, onFailure = { false })

    override suspend fun logOut() {
        tokenStorage.token = null
    }

    override suspend fun isAuthorized() = tokenStorage.token != null

    override suspend fun getUserData() =
        userService.getUserData(userId = JWT.decode(tokenStorage.token).subject).getOrNull()
}