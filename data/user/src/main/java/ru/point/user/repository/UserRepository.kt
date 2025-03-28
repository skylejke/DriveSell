package ru.point.user.repository

import ru.point.user.model.DeleteUserResponse
import ru.point.user.model.EditUserDataRequest
import ru.point.user.model.EditUserPasswordRequest
import ru.point.user.model.LoginRequest
import ru.point.user.model.LoginResponse
import ru.point.user.model.RegisterRequest
import ru.point.user.model.RegisterResponse
import ru.point.user.model.EditDataResponse
import ru.point.user.model.EditPasswordResponse
import ru.point.user.model.UserDataResponse

interface UserRepository {
    suspend fun login(loginRequest: LoginRequest): Result<LoginResponse>
    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse>
    suspend fun logOut()
    suspend fun isAuthorized(): Boolean
    suspend fun getUserData(): UserDataResponse?
    suspend fun editUserData(editUserDataRequest: EditUserDataRequest): Result<EditDataResponse>
    suspend fun editPassword(editUserPasswordRequest: EditUserPasswordRequest): Result<EditPasswordResponse>
    suspend fun deleteProfile(): Result<DeleteUserResponse>
}