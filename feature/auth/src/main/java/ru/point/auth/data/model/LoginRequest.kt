package ru.point.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    @SerialName("username")
    val username: String,
    @SerialName("password")
    val password: String
)
