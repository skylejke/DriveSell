package ru.point.cars.model

import kotlinx.serialization.Serializable

@Serializable
data class AdDto(
    val id: String,
    val creationDate: String,
    val userId: String,
    val car: CarDto,
    val photos: List<String>
)
