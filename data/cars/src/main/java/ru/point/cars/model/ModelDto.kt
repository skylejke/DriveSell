package ru.point.cars.model

import kotlinx.serialization.Serializable

@Serializable
data class ModelDto(
    val id: Int,
    val name: String,
    val brandName: String,
)