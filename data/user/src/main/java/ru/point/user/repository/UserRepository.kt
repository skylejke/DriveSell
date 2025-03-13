package ru.point.user.repository

import ru.point.user.model.LoginRequest
import ru.point.user.model.RegisterRequest
import ru.point.user.model.UserDataResponse

interface UserRepository {
    suspend fun login(loginRequest: LoginRequest): Boolean
    suspend fun register(registerRequest: RegisterRequest): Boolean
    suspend fun logOut()
    suspend fun isAuthorized(): Boolean
    suspend fun getUserData(): UserDataResponse?
}