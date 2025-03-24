package ru.point.profile.domain

import ru.point.user.repository.UserRepository

internal class DeleteProfileUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.deleteProfile()
}