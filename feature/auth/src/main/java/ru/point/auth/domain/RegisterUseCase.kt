package ru.point.auth.domain

import ru.point.user.model.RegisterRequest
import ru.point.user.repository.UserRepository

internal class RegisterUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(registerRequest: RegisterRequest) =
        userRepository.register(registerRequest = registerRequest)
}