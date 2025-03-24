package ru.point.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditUserDataRequest(
    @SerialName("username") val username: String? = null,
    @SerialName("email") val email: String? = null,
    @SerialName("phoneNumber") val phoneNumber: String? = null,
)

