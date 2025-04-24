package ru.point.user.repository

import ru.point.common.model.ResponseMessage
import ru.point.user.model.AuthResponse
import ru.point.user.model.EditUserDataRequest
import ru.point.user.model.EditUserPasswordRequest
import ru.point.user.model.LoginRequest
import ru.point.user.model.RegisterRequest
import ru.point.user.model.UserData

interface UserRepository {

    suspend fun getUserId(): String

    suspend fun login(loginRequest: LoginRequest): Result<AuthResponse>

    suspend fun register(registerRequest: RegisterRequest): Result<AuthResponse>

    suspend fun logOut()

    suspend fun isAuthorized(): Boolean

    suspend fun getUserData(): Result<UserData?>

    suspend fun editUserData(editUserDataRequest: EditUserDataRequest): Result<ResponseMessage>

    suspend fun editPassword(editUserPasswordRequest: EditUserPasswordRequest): Result<ResponseMessage>

    suspend fun deleteProfile(): Result<ResponseMessage>
}