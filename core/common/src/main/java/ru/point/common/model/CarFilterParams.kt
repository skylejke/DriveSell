package ru.point.common.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarFilterParams(
    val query: String? = null,
    val brand: String? = null,
    val model: String? = null,
    val yearMin: Short? = null,
    val yearMax: Short? = null,
    val priceMin: Int? = null,
    val priceMax: Int? = null,
    val mileageMin: Int? = null,
    val mileageMax: Int? = null,
    val enginePowerMin: Short? = null,
    val enginePowerMax: Short? = null,
    val engineCapacityMin: Double? = null,
    val engineCapacityMax: Double? = null,
    val fuelType: String? = null,
    val bodyType: String? = null,
    val color: String? = null,
    val transmission: String? = null,
    val drivetrain: String? = null,
    val wheel: String? = null,
    val condition: String? = null,
    val owners: String? = null
) : Parcelable

fun CarFilterParams.toQueryMap(): Map<String, String> = listOfNotNull(
    query?.takeIf(String::isNotBlank)?.let { "query" to it },
    brand?.takeIf(String::isNotBlank)?.let { "brand" to it },
    model?.takeIf(String::isNotBlank)?.let { "model" to it },
    yearMin?.toString()?.let { "yearMin" to it },
    yearMax?.toString()?.let { "yearMax" to it },
    priceMin?.toString()?.let { "priceMin" to it },
    priceMax?.toString()?.let { "priceMax" to it },
    mileageMin?.toString()?.let { "mileageMin" to it },
    mileageMax?.toString()?.let { "mileageMax" to it },
    enginePowerMin?.toString()?.let { "enginePowerMin" to it },
    enginePowerMax?.toString()?.let { "enginePowerMax" to it },
    engineCapacityMin?.toString()?.let { "engineCapacityMin" to it },
    engineCapacityMax?.toString()?.let { "engineCapacityMax" to it },
    fuelType?.takeIf(String::isNotBlank)?.let { "fuelType" to it },
    bodyType?.takeIf(String::isNotBlank)?.let { "bodyType" to it },
    color?.takeIf(String::isNotBlank)?.let { "color" to it },
    transmission?.takeIf(String::isNotBlank)?.let { "transmission" to it },
    drivetrain?.takeIf(String::isNotBlank)?.let { "drivetrain" to it },
    wheel?.takeIf(String::isNotBlank)?.let { "wheel" to it },
    condition?.takeIf(String::isNotBlank)?.let { "condition" to it },
    owners?.takeIf(String::isNotBlank)?.let { "owners" to it }
).toMap()
