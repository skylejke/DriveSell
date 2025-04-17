package ru.point.cars.model.enums

enum class OrderParams {
    ASC,
    DESC;

    override fun toString() = name.lowercase()
}