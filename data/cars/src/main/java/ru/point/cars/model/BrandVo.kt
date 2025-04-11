package ru.point.cars.model


data class BrandVo(val name: String)

val BrandDto.asBrandVo
    get() = BrandVo(name = name)