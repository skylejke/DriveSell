package ru.point.auth.domain

import ru.point.auth.data.AuthRepository
import ru.point.auth.data.model.RegisterRequest

class RegisterUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(registerRequest: RegisterRequest) {
        TODO()
    }
}