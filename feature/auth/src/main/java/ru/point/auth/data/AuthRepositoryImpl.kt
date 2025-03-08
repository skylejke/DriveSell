package ru.point.auth.data

import ru.point.auth.data.model.LoginRequest
import ru.point.auth.data.model.RegisterRequest
import ru.point.auth.storage.AuthStorage

internal class AuthRepositoryImpl(
    private val authService: AuthService,
    private val authStorage: AuthStorage
) : AuthRepository {
    override suspend fun login(loginRequest: LoginRequest) =
        authService.login(loginRequest)

    override suspend fun loginWithToken() = authStorage.token != null

    override suspend fun register(registerRequest: RegisterRequest) =
        authService.register(registerRequest)
}