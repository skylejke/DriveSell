package ru.point.auth.data

import ru.point.auth.data.model.LoginRequest
import ru.point.auth.data.model.RegisterRequest
import ru.point.auth.data.model.Token

interface AuthRepository {
    suspend fun login(loginRequest: LoginRequest): Result<Token>
    suspend fun register(registerRequest: RegisterRequest): Result<Token>
}