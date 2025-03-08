package ru.point.auth.data

import retrofit2.http.Body
import retrofit2.http.POST
import ru.point.auth.data.model.LoginRequest
import ru.point.auth.data.model.RegisterRequest
import ru.point.auth.data.model.Token

internal interface AuthService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Result<Token>

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Result<Token>
}