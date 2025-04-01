package ru.point.user.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val token: Token? = null,
    val message: String? = null
)
