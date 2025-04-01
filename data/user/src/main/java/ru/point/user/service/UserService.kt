package ru.point.user.service

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import ru.point.common.model.ResponseMessage
import ru.point.user.model.AuthResponse
import ru.point.user.model.EditUserDataRequest
import ru.point.user.model.EditUserPasswordRequest
import ru.point.user.model.LoginRequest
import ru.point.user.model.RegisterRequest
import ru.point.user.model.UserData

interface UserService {
    @POST("/login")
    suspend fun login(@Body loginRequest: LoginRequest): Result<AuthResponse>

    @POST("/register")
    suspend fun register(@Body registerRequest: RegisterRequest): Result<AuthResponse>

    @GET("/profile/{userId}")
    suspend fun getUserData(@Path("userId") userId: String): Result<UserData>

    @PATCH("/profile/{userId}")
    suspend fun editUserData(
        @Path("userId") userId: String,
        @Body editUserDataRequest: EditUserDataRequest
    ): Result<ResponseMessage>

    @PATCH("/profile/{userId}/updatePassword")
    suspend fun editPassword(
        @Path("userId") userId: String,
        @Body editUserPasswordRequest: EditUserPasswordRequest
    ): Result<ResponseMessage>

    @DELETE("/profile/{userId}")
    suspend fun deleteProfile(@Path("userId") userId: String): Result<ResponseMessage>
}