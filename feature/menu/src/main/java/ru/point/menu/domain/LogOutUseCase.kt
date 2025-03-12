package ru.point.menu.domain

import ru.point.user.repository.UserRepository

internal class LogOutUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.logOut()
}