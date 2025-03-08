package ru.point.auth.domain

import ru.point.auth.data.AuthRepository

internal class LoginWithTokenUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke() = authRepository.loginWithToken()
}