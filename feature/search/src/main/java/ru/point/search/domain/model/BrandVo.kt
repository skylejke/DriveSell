package ru.point.search.domain.model

import kotlinx.serialization.Serializable
import ru.point.cars.model.BrandDto

@Serializable
data class BrandVo(val name: String)

val BrandDto.asBrandVo
    get() = BrandVo(name = name)