package ru.point.auth.domain

import ru.point.user.model.LoginRequest
import ru.point.user.repository.UserRepository

internal class LoginUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(loginRequest: LoginRequest) =
        userRepository.login(loginRequest = loginRequest)
}
