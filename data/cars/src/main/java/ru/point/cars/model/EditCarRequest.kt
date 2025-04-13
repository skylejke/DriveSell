package ru.point.cars.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@Serializable
data class EditCarRequest(
    val brand: String? = null,
    val model: String? = null,
    val year: Short? = null,
    val price: Int? = null,
    val mileage: Int? = null,
    val enginePower: Short? = null,
    val engineCapacity: Double? = null,
    val fuelType: String? = null,
    val bodyType: String? = null,
    val color: String? = null,
    val transmission: String? = null,
    val driveTrain: String? = null,
    val wheel: String? = null,
    val condition: String? = null,
    val owners: Byte? = null,
    val vin: String? = null,
    val ownershipPeriod: String? = null,
    val description: String? = null
)

val EditCarRequest.asRequestBody
    get() = Json.encodeToString(EditCarRequest.serializer(), this)
        .toRequestBody("application/json".toMediaTypeOrNull())