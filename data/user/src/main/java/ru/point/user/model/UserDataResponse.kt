package ru.point.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDataResponse(
    @SerialName("username") val username: String,
    @SerialName("email") val email: String,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("password") val password: String
)
