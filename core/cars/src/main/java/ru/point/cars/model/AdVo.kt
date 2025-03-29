package ru.point.cars.model

import kotlinx.serialization.Serializable

@Serializable
data class AdVo(
    val id: String,
    val creationDate: String,
    val userId: String,
    val car: CarVo,
    val photos: List<String>
)

val AdDto.asAdVo
    get() = AdVo(
        id = id,
        creationDate = creationDate,
        userId = userId,
        car = car.asCarVo,
        photos = photos
    )
