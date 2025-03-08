package ru.point.auth.domain

import ru.point.auth.data.AuthRepository
import ru.point.auth.data.model.LoginRequest
import ru.point.auth.storage.AuthStorage

internal class LoginUseCase(
    private val authRepository: AuthRepository,
    private val authStorage: AuthStorage
) {
    suspend operator fun invoke(loginRequest: LoginRequest) =
        authRepository.login(loginRequest = loginRequest)
            .fold(onSuccess = { token ->
                authStorage.token = token.token
                true
            }, onFailure = { false })
}
