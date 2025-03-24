package ru.point.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EditUserPasswordRequest(
    @SerialName("oldPassword") val oldPassword: String,
    @SerialName("newPassword") val newPassword: String,
    @SerialName("confirmNewPassword") val confirmNewPassword: String
)
