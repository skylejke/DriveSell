package ru.point.auth.data

import ru.point.auth.data.model.LoginRequest
import ru.point.auth.data.model.RegisterRequest

class AuthRepositoryImpl(private val authService: AuthService) : AuthRepository {
    override suspend fun login(loginRequest: LoginRequest) =
        authService.login(loginRequest)

    override suspend fun register(registerRequest: RegisterRequest) =
        authService.register(registerRequest)
}