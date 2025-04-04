package ru.point.cars.model

data class CarFilters(
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
)

fun CarFilters.toQueryMap(): Map<String, String> {
    return mutableMapOf<String, String>().apply {
        brand?.let { put("brand", it) }
        model?.let { put("model", it) }
        yearMin?.let { put("yearMin", it.toString()) }
        yearMax?.let { put("yearMax", it.toString()) }
        priceMin?.let { put("priceMin", it.toString()) }
        priceMax?.let { put("priceMax", it.toString()) }
        mileageMin?.let { put("mileageMin", it.toString()) }
        mileageMax?.let { put("mileageMax", it.toString()) }
        enginePowerMin?.let { put("enginePowerMin", it.toString()) }
        enginePowerMax?.let { put("enginePowerMax", it.toString()) }
        engineCapacityMin?.let { put("engineCapacityMin", it.toString()) }
        engineCapacityMax?.let { put("engineCapacityMax", it.toString()) }
        fuelType?.let { put("fuelType", it) }
        bodyType?.let { put("bodyType", it) }
        color?.let { put("color", it) }
        transmission?.let { put("transmission", it) }
        drivetrain?.let { put("drivetrain", it) }
        wheel?.let { put("wheel", it) }
        condition?.let { put("condition", it) }
        owners?.let { put("owners", it) }
    }
}
