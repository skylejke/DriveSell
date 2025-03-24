package ru.point.profile.domain

import ru.point.user.model.EditUserDataRequest
import ru.point.user.repository.UserRepository

internal class EditUserDataUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(editUserDataRequest: EditUserDataRequest) =
        userRepository.editUserData(editUserDataRequest = editUserDataRequest)
}