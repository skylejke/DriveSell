package ru.point.cars.model

import kotlinx.serialization.Serializable

@Serializable
data class BrandDto(
    val id: Int,
    val name: String
)
