package ru.point.profile.domain

import ru.point.user.model.EditUserPasswordRequest
import ru.point.user.repository.UserRepository

internal class EditPasswordUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(editUserPasswordRequest: EditUserPasswordRequest) =
        userRepository.editPassword(editUserPasswordRequest = editUserPasswordRequest)
}