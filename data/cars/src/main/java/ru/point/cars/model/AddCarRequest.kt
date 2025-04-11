package ru.point.cars.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class AddCarRequest(
    val brand: String,
    val model: String,
    val year: Short,
    val price: Int,
    val enginePower: Short,
    val engineCapacity: Double,
    val fuelType: String,
    val mileage: Int,
    val bodyType: String,
    val color: String,
    val transmission: String,
    val drivetrain: String,
    val wheel: String,
    val condition: String,
    val owners: Byte,
    val vin: String,
    val ownershipPeriod: String,
    val description: String
)

val AddCarRequest.asRequestBody
    get() = Json.encodeToString(AddCarRequest.serializer(), this)
        .toRequestBody("application/json".toMediaTypeOrNull())
