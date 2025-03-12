package ru.point.user.service

import retrofit2.http.Body
import retrofit2.http.POST
import ru.point.user.model.LoginRequest
import ru.point.user.model.RegisterRequest
import ru.point.user.model.Token

interface UserService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Result<Token>

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Result<Token>
}