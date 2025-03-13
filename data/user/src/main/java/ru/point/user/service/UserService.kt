package ru.point.user.service

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.point.user.model.LoginRequest
import ru.point.user.model.RegisterRequest
import ru.point.user.model.Token
import ru.point.user.model.UserDataResponse

interface UserService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Result<Token>

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Result<Token>

    @GET("/profile/{userId}")
    suspend fun getUserData(@Path("userId") userId: String): Result<UserDataResponse>
}