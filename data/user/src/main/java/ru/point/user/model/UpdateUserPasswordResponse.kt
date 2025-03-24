package ru.point.user.model

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserPasswordResponse(val message: String)