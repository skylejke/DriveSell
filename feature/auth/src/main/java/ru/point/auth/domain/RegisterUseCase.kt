package ru.point.auth.domain

import ru.point.auth.data.AuthRepository
import ru.point.auth.data.model.RegisterRequest
import ru.point.auth.storage.AuthStorage

internal class RegisterUseCase(
    private val authRepository: AuthRepository,
    private val authStorage: AuthStorage
) {
    suspend operator fun invoke(registerRequest: RegisterRequest) =
        authRepository.register(registerRequest = registerRequest)
            .fold(onSuccess = { token ->
                authStorage.token = token.token
                true
            }, onFailure = { false })
}