package ru.point.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Token(
    @SerialName("token") val token: String,
)
