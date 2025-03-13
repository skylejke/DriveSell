package ru.point.profile.domain

import ru.point.user.repository.UserRepository

internal class GetUserDataUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() =
        userRepository.getUserData()
}