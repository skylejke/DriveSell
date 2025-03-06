package ru.point.auth.domain

import ru.point.auth.data.AuthRepository
import ru.point.auth.data.model.LoginRequest

class LoginUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(loginRequest: LoginRequest) {
        TODO()
    }
}